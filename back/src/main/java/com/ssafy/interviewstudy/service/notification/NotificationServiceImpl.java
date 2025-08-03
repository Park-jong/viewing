package com.ssafy.interviewstudy.service.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.notification.Notification;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.domain.study.StudyMember;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.dto.notification.NotificationListDto;
import com.ssafy.interviewstudy.dto.notification.NotificationStudyDto;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.notification.EmitterRepository;
import com.ssafy.interviewstudy.repository.notification.NotificationRepository;
import com.ssafy.interviewstudy.repository.study.StudyMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    //JSON화 시키는 Object Mapper
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final NotificationDtoManager notificationDtoManager;
    private static final Long sseEmitterTimeOut = 3600L * 60L * 60L * 24L;

    @Override
    public SseEmitter connect(Integer memberId, Integer lastEventId) {
        String timeIncludeId = memberId + "_" + System.currentTimeMillis();
        SseEmitter sseEmitter = emitterRepository.save(timeIncludeId, new SseEmitter(sseEmitterTimeOut));

        //연결 완료시 삭제 (연결 시간이 끝남)
        sseEmitter.onCompletion(() -> emitterRepository.deleteSseEmitterById(timeIncludeId));
        //타임 아웃시 삭제 (데이터를 안보냄)
        sseEmitter.onTimeout(() -> emitterRepository.deleteSseEmitterById(timeIncludeId));
        sseEmitter.onError((e) -> emitterRepository.deleteSseEmitterById(timeIncludeId));
        //SSE는 초기에 메세지를 보내지 않으면 오류발생 (더미 이벤트 만들자)
        sendEventByEmitter(
                sseEmitter,
                NotificationDto
                        .builder()
                        .notificationType(NotificationType.Test)
                        .content("연결 테스트")
                        .notificationId(0)
                        .memberId(1)
                        .build(),
                timeIncludeId,
                0,
                "test"
        );
        if (lastEventId == null) lastEventId = 0;
        sendMissingData(lastEventId, memberId, timeIncludeId, sseEmitter);
        return sseEmitter;

    }

    @Override
    public void sendEventByEmitter(SseEmitter sseEmitter, Object notificationDto, String emitterId, Integer eventId, String eventName) {
        try {
            sseEmitter.send(
                    SseEmitter
                            .event()
                            .id(eventId.toString())
                            .name(eventName)
                            .data(objectMapper.writeValueAsString(notificationDto))
            );
        } catch (IOException e) {
            emitterRepository.deleteSseEmitterById(emitterId);
        }
    }

    @Transactional
    @Override
    public void sendNotificationAboutComment(ArticleComment comment, NotificationType type) {
        Integer receiver = comment.getArticle().getAuthor().getId();
        String content = getNotificationContentByType(comment, type);
        String url = getUrl(comment);
        NotificationDto notification = notificationDtoManager.makeNotification(receiver, content, type, url, false);
        sendNotificationToMember(notification);
    }

    private String getNotificationContentByType(ArticleComment comment, NotificationType type) {
        if (type == NotificationType.BoardComment) {
            return newCommentNotification(comment);
        } else if (type == NotificationType.BoardReply) {
            return newReplyNotification(comment);
        } else {
            return "";
        }
    }

    private String newCommentNotification(ArticleComment comment) {
        return String.format("게시글 \"%s\"에 댓글이 달렸습니다.", comment.getArticle().getTitle());
    }

    private String newReplyNotification(ArticleComment comment) {
        return "댓글에 대댓글이 달렸습니다.";
    }

    private String getUrl(ArticleComment comment) {
        return String.format("%s %s", boardTypeToUrl(comment.getArticle().getBoardType()), comment.getArticle().getId().toString());
    }

    private String boardTypeToUrl(BoardType boardType) {
        if (boardType == BoardType.review) {
            return "interview";
        }
        if (boardType == BoardType.qna) {
            return "question";
        }
        if (boardType == BoardType.general) {
            return "free";
        }
        return "free";
    }

    @Transactional
    @Override
    public void sendNotificationToMember(NotificationDto notificationDto) {
        Notification notification = dtoToEntity(notificationDto);
        notificationRepository.save(notification);
        Map<String, SseEmitter> memberEmitters = emitterRepository.getEmittersByMemberId(notificationDto.getMemberId());
        String eventId = notificationDto.getMemberId() + "_" + System.currentTimeMillis();
        memberEmitters.forEach((emitterId, sseEmitter) -> sendEventByEmitter(sseEmitter, NotificationDto.fromEntity(notification), emitterId, notification.getId(), "notification"));

    }

    @Transactional
    @Override
    public void sendNotificationToStudyMember(NotificationStudyDto notificationStudyDto) {
        List<StudyMember> studyMembers = studyMemberRepository.findMembersByStudyId(notificationStudyDto.getStudyId());
        NotificationDto notificationDto = notificationStudyDto.getNotificationDto();
        studyMembers.forEach(
                (studyMember) -> {
                    Integer memberId = studyMember.getMember().getId();
                    NotificationDto newNotificationDto = NotificationDto.changeMember(notificationDto, memberId);
                    sendNotificationToMember(newNotificationDto);
                }
        );
    }

    @Transactional
    @Override
    public Notification dtoToEntity(@Valid NotificationDto notificationDto) {
        Member author = memberRepository.findMemberById(notificationDto.getMemberId());
        Notification notification =
                Notification
                        .builder()
                        .notificationType(notificationDto.getNotificationType())
                        .content(notificationDto.getContent())
                        .url(notificationDto.getUrl())
                        .author(author)
                        .build();
        return notification;
    }

    @Transactional
    @Override
    public void sendMissingData(Integer lastEventId, Integer memberId, String emitterId, SseEmitter sseEmitter) {
        List<Notification> missingNotificationList = notificationRepository.findTop20ByIdGreaterThanAndAuthorIdOrderByCreatedAtDesc(lastEventId, memberId);
        NotificationListDto notificationListDto = NotificationListDto.noticiationListDtoFromListNotification(missingNotificationList);
        sendEventByEmitter(sseEmitter, notificationListDto, emitterId, lastEventId, "lastNotification");
    }

    @Transactional
    @Override
    public Boolean checkNotification(Integer notificationId) {
        Notification notification = notificationRepository.findNotificationById(notificationId);
        if (notification == null) {
            return false;
        }
        notification.readNotification();
        return true;
    }


    @Transactional
    @Override
    public Boolean checkNotificationByMemberId(Integer memberId, Integer notificationId) {
        return notificationRepository.findNotificationByAuthorIdAndId(memberId, notificationId) != null;
    }
}
