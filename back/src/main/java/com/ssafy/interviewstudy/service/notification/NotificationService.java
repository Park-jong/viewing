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

    //특정 멤버에게 이벤트 보내기
    void sendNotificationToMember(NotificationDto notificationDto);

    void sendNotificationAboutComment(ArticleComment comment, NotificationType type);

    void sendNotificationToStudyMember(NotificationStudyDto notificationStudyDto);

    Notification dtoToEntity(@Valid NotificationDto notificationDto);

    void sendMissingData(Integer lastEventId, Integer memberId, String emitterId, SseEmitter sseEmitter);

    Boolean checkNotification(Integer notificationId);

    Boolean checkNotificationByMemberId(Integer memberId, Integer notificationId);
}
