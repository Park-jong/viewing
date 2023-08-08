package com.ssafy.interviewstudy.dto.notification;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class NotificationStudyDto {

    //알림을 보낼 스터디
    private Integer studyId;

    private NotificationDto notificationDto;

    @Builder
    public NotificationStudyDto(Integer studyId, NotificationDto notificationDto) {
        this.studyId = studyId;
        this.notificationDto = notificationDto;
    }
}
