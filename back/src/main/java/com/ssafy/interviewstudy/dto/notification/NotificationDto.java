package com.ssafy.interviewstudy.dto.notification;

import com.ssafy.interviewstudy.domain.notification.Notification;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class NotificationDto {

    private Integer notificationId;

    @NotNull
    private Integer memberId;

    @NotNull
    private String content;

    @NotNull
    private NotificationType notificationType;

    private String url = null;

    private Boolean isRead;

    private LocalDateTime createdAt;

    @Builder
    public NotificationDto(Integer notificationId, Integer memberId, String content, NotificationType notificationType, String url, Boolean isRead, LocalDateTime createdAt) {
        this.notificationId = notificationId;
        this.memberId = memberId;
        this.content = content;
        this.notificationType = notificationType;
        this.url = url;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public static NotificationDto fromEntity(Notification notification){
        return NotificationDto
                .builder()
                .content(notification.getContent())
                .createdAt(notification.getCreatedAt())
                .isRead(notification.getIsRead())
                .notificationId(notification.getId())
                .url(notification.getUrl())
                .notificationType(notification.getNotificationType())
                .memberId(notification.getAuthor().getId())
                .build();
    }
}
