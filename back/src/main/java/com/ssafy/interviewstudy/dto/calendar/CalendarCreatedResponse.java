package com.ssafy.interviewstudy.dto.calendar;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CalendarCreatedResponse {
    private Integer calendarId;

    public CalendarCreatedResponse(Integer calendarId) {
        this.calendarId = calendarId;
    }
}
