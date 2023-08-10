package com.ssafy.interviewstudy.dto.notification;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    //알림 ID (PK)
    private Integer notificationId;

    //알림 수신자
    @NotNull
    private Integer memberId;

    //알림 내용
    @NotNull
    private String content;

    //알림 종류
    @NotNull
    private NotificationType notificationType;

    //이동할 url
    private String url = null;

    //읽음 여부
    private Boolean isRead;

    //알림 생성일
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
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

    public static NotificationDto changeMember(NotificationDto notificationDto,Integer changeMemberId){
        return NotificationDto
                .builder()
                .notificationType(notificationDto.getNotificationType())
                .memberId(changeMemberId)
                .content(notificationDto.getContent())
                .url(notificationDto.getUrl())
                .isRead(notificationDto.isRead)
                .build();
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
