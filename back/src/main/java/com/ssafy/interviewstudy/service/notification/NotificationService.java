package com.ssafy.interviewstudy.service.notification;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.notification.Notification;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.dto.notification.NotificationStudyDto;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.validation.Valid;

public interface NotificationService {
    SseEmitter connect(Integer memberId, Integer lastEventId);

    void sendEventByEmitter(SseEmitter sseEmitter, Object notificationDto, String emitterId, Integer eventId, String eventName);

    @Transactional
        //특정 멤버에게 이벤트 보내기
    void sendNotificationToMember(NotificationDto notificationDto);

    @Transactional
    void sendNotificationAboutComment(ArticleComment comment, NotificationType type);

    @Transactional
    void sendNotificationToStudyMember(NotificationStudyDto notificationStudyDto);

    @Transactional
    Notification dtoToEntity(@Valid NotificationDto notificationDto);

    @Transactional
    void sendMissingData(Integer lastEventId, Integer memberId, String emitterId, SseEmitter sseEmitter);

    @Transactional
    Boolean checkNotification(Integer notificationId);

    @Transactional
    Boolean checkNotificationByMemberId(Integer memberId, Integer notificationId);
}
