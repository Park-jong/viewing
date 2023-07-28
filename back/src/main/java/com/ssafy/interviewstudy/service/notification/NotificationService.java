package com.ssafy.interviewstudy.service.notification;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.notification.Notification;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.notification.EmitterRepository;
import com.ssafy.interviewstudy.repository.notification.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class NotificationService {

    //알림 CRUD 레포지토리
    private final NotificationRepository notificationRepository;

    //SSE 연결 핸들러 레포지토리
    private final EmitterRepository emitterRepository;

    //멤버 리포지토리
    private final MemberRepository memberRepository;

    //SSE 연결시간
    private final Long sseEmitterTimeOut= 60L*60L*24L;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository, EmitterRepository emitterRepository, MemberRepository memberRepository) {
        this.notificationRepository = notificationRepository;
        this.emitterRepository = emitterRepository;
        this.memberRepository = memberRepository;
    }



    public SseEmitter connect(Integer memberId, Integer lastEventId){
        String timeIncludeId = memberId+"_"+System.currentTimeMillis();
        SseEmitter sseEmitter = emitterRepository.save(timeIncludeId,new SseEmitter(sseEmitterTimeOut));

        //연결 완료시 삭제 (연결 시간이 끝남)
        sseEmitter.onCompletion(()->emitterRepository.deleteSseEmitterById(timeIncludeId));

        //타임 아웃시 삭제 (데이터를 안보냄)
        sseEmitter.onTimeout(()->emitterRepository.deleteSseEmitterById(timeIncludeId));

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
                0

        );
        if(lastEventId!=null){
            sendMissingData(lastEventId,memberId,timeIncludeId,sseEmitter);
        }

        return sseEmitter;

    }

    public void sendEventByEmitter(SseEmitter sseEmitter,  NotificationDto notificationDto, String emitterId, Integer eventId){
        try{
            sseEmitter.send(
                    SseEmitter
                            .event()
                            .id(eventId.toString())
                            .name("notification")
                            .data(notificationDto)
            );
        }
        catch(IOException e){
            emitterRepository.deleteSseEmitterById(emitterId);
        }
    }

    //특정 멤버에게 이벤트 보내기
    @Transactional
    public void sendNotificationToMember(NotificationDto notificationDto){

        Notification notification = dtoToEntity(notificationDto);
        notificationRepository.save(notification);

        Map<String,SseEmitter> memberEmitters =
                emitterRepository.getEmittersByMemberId(notificationDto.getMemberId());

        String eventId = notificationDto.getMemberId()+"_"+System.currentTimeMillis();

        memberEmitters.forEach(
                (emitterId,sseEmitter)->{
                    sendEventByEmitter(sseEmitter,notificationDto,emitterId, notification.getId());
                }
        );

    }

    @Transactional
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
    public void sendMissingData(Integer lastEventId,Integer memberId,String emitterId,SseEmitter sseEmitter){
        List<Notification> missingNotificationList =
                notificationRepository.findNotificationsByIdGreaterThanAndAuthorId(lastEventId,memberId);
        for(Notification notification : missingNotificationList){
            sendEventByEmitter(sseEmitter,
                    NotificationDto.fromEntity(notification),
                    emitterId,
                    notification.getId()
            );
        }

    }
}
