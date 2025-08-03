package com.ssafy.interviewstudy.service.notification;

import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationDtoManager {

    public NotificationDto makeNotification(Integer receiver, String content, NotificationType type, String url, boolean isRead) {
        return NotificationDto
                .builder()
                .memberId(receiver)
                .content(content)
                .notificationType(type)
                .url(url)
                .isRead(isRead)
                .build();
    }
}
