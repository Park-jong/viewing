package com.ssafy.interviewstudy.dto.study;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.study.StudyCalendar;
import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StudyCalendarDtoResponse {
    private Integer calendarId;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startedAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endedAt;

    private String description;

    private StudyMemberDto author;

    Boolean isStudyCalendar = true;

    public StudyCalendarDtoResponse(StudyCalendar studyCalendar){
        this.calendarId = studyCalendar.getId();
        this.startedAt = studyCalendar.getStartedAt();
        this.endedAt = studyCalendar.getEndedAt();
        this.description = studyCalendar.getDescription();
        this.author = new StudyMemberDto(studyCalendar.getAuthor());
    }
}
