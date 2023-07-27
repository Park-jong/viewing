package com.ssafy.interviewstudy.domain.study;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.study.StudyCalendarDtoRequest;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class StudyCalendar {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_calender_id")
    private Integer id;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id")
    private Study study;

    public StudyCalendar(Study study, Member member, StudyCalendarDtoRequest studyCalendarDtoRequest){
        this.study = study;
        this.author = member;
        this.startedAt = studyCalendarDtoRequest.getStartedAt();
        this.endedAt = studyCalendarDtoRequest.getEndedAt();
        this.description = studyCalendarDtoRequest.getDescription();
    }

    public void updateCalendar(StudyCalendarDtoRequest studyCalendarDtoRequest) {
        this.startedAt = studyCalendarDtoRequest.getStartedAt();
        this.endedAt = studyCalendarDtoRequest.getEndedAt();
        this.description = studyCalendarDtoRequest.getDescription();
    }
}
