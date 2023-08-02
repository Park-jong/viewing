package com.ssafy.interviewstudy.dto.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.calendar.Calendar;
import com.ssafy.interviewstudy.domain.member.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CalendarRetrieveRequest {

    private Integer calendarId;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime startedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime endedAt;

    private String description;

    private Integer memberId;


    public static Calendar toEntity(CalendarRetrieveRequest calendarDto, Member author){
        Calendar calendar =
                Calendar.builder()
                        .id(calendarDto.getCalendarId())
                        .startedAt(calendarDto.getStartedAt())
                        .endedAt(calendarDto.getEndedAt())
                        .description(calendarDto.getDescription())
                        .author(author)
                        .build();
        return calendar;
    }
}
