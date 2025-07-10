package com.ssafy.interviewstudy.service.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
    private final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    //알림 CRUD 레포지토리 
    private final NotificationRepository notificationRepository;

    //SSE 연결 핸들러 레포지토리
    private final EmitterRepository emitterRepository;

    //멤버 리포지토리
    private final MemberRepository memberRepository;

    private final StudyMemberRepository studyMemberRepository;

    //SSE 연결시간
    private final Long sseEmitterTimeOut= 3600L*60L*60L*24L;


    @Override
    public SseEmitter connect(Integer memberId, Integer lastEventId){
        String timeIncludeId = memberId+"_"+System.currentTimeMillis();
        SseEmitter sseEmitter = emitterRepository.save(timeIncludeId,new SseEmitter(sseEmitterTimeOut));

        //연결 완료시 삭제 (연결 시간이 끝남)
        sseEmitter.onCompletion(
                ()->{
                    emitterRepository.deleteSseEmitterById(timeIncludeId);
                }
        );

        //타임 아웃시 삭제 (데이터를 안보냄)
        sseEmitter.onTimeout(
                ()->{
                    emitterRepository.deleteSseEmitterById(timeIncludeId);
                }
        );

        sseEmitter.onError(
                (e)-> emitterRepository.deleteSseEmitterById(timeIncludeId)
        );

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
        if(lastEventId==null) lastEventId=0;
        sendMissingData(lastEventId,memberId,timeIncludeId,sseEmitter);
        return sseEmitter;

    }

    @Override
    public void sendEventByEmitter(SseEmitter sseEmitter, Object notificationDto, String emitterId, Integer eventId, String eventName){
        try{
            sseEmitter.send(
                    SseEmitter
                            .event()
                            .id(eventId.toString())
                            .name(eventName)
                            .data(objectMapper.writeValueAsString(notificationDto))
            );
        }
        catch(IOException e){
            emitterRepository.deleteSseEmitterById(emitterId);
        }
    }

    @Transactional
    @Override
    public void sendNotificationToMember(NotificationDto notificationDto){

        Notification notification = dtoToEntity(notificationDto);
        notificationRepository.save(notification);

        Map<String,SseEmitter> memberEmitters =
                emitterRepository.getEmittersByMemberId(notificationDto.getMemberId());

        String eventId = notificationDto.getMemberId()+"_"+System.currentTimeMillis();
        memberEmitters.forEach(
                (emitterId,sseEmitter)->{
                    sendEventByEmitter(sseEmitter,NotificationDto.fromEntity(notification),emitterId, notification.getId(),"notification");
                }
        );

    }

    @Transactional
    @Override
    public void sendNotificationToStudyMember(NotificationStudyDto notificationStudyDto){
        List<StudyMember> studyMembers = studyMemberRepository.findMembersByStudyId(notificationStudyDto.getStudyId());
        NotificationDto notificationDto = notificationStudyDto.getNotificationDto();
        studyMembers.forEach(
                (studyMember)->{
                    Integer memberId = studyMember.getMember().getId();
                    sendNotificationToMember(
                            NotificationDto.changeMember(notificationDto,memberId)
                    );
                }
        );
    }

    @Transactional
    @Override
    public Notification dtoToEntity(@Valid NotificationDto notificationDto){
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
    public void sendMissingData(Integer lastEventId, Integer memberId, String emitterId, SseEmitter sseEmitter){
        List<Notification> missingNotificationList =
                notificationRepository.findTop20ByIdGreaterThanAndAuthorIdOrderByCreatedAtDesc(lastEventId,memberId);

        NotificationListDto notificationListDto =
                NotificationListDto.noticiationListDtoFromListNotification(missingNotificationList);

        sendEventByEmitter(sseEmitter,notificationListDto,emitterId,lastEventId,"lastNotification");
    }

    @Transactional
    @Override
    public Boolean checkNotification(Integer notificationId){
        Notification notification = notificationRepository.findNotificationById(notificationId);
        if(notification==null) return false;
        notification.readNotification();
        return true;
    }


    @Transactional
    @Override
    public Boolean checkNotificationByMemberId(Integer memberId, Integer notificationId){
        return notificationRepository.findNotificationByAuthorIdAndId(memberId,notificationId) != null;
    }
}
