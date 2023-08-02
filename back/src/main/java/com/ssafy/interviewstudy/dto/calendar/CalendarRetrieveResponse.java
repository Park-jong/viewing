package com.ssafy.interviewstudy.dto.calendar;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.calendar.Calendar;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.Author;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CalendarRetrieveResponse {
    private Integer id;

    private Author author;

    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime startedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    @NotNull
    private LocalDateTime endedAt;

    Boolean isStudyCalendar = false;

    @Builder
    public CalendarRetrieveResponse(Integer id, Author author, String description, LocalDateTime startedAt, LocalDateTime endedAt) {
        this.id = id;
        this.author = author;
        this.description = description;
        this.startedAt = startedAt;
        this.endedAt = endedAt;
    }

    public static CalendarRetrieveResponse fromEntity(Calendar calendar){
        CalendarRetrieveResponse calendarRetrieveResponse
                = CalendarRetrieveResponse
                .builder()
                .id(calendar.getId())
                .author(new Author(calendar.getAuthor()))
                .description(calendar.getDescription())
                .startedAt(calendar.getStartedAt())
                .endedAt(calendar.getEndedAt())
                .build();
        return calendarRetrieveResponse;
    }
}
