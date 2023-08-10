package com.ssafy.interviewstudy.dto.notification;

import com.ssafy.interviewstudy.domain.notification.Notification;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.modeler.NotificationInfo;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class NotificationListDto {

    private List<NotificationDto> notificationDtoList = new ArrayList<>();

    private Integer dataSize = 0;

    private void addNotificationDto(NotificationDto noticiationDto){
        notificationDtoList.add(noticiationDto);
        dataSize++;
    }

    public static NotificationListDto noticiationListDtoFromListNotification(List<Notification> notificationList){
        NotificationListDto notificationListDto = new NotificationListDto();
        for(Notification notification : notificationList){
            notificationListDto.addNotificationDto(NotificationDto.fromEntity(notification));
        }
        return notificationListDto;
    }
}
