package com.ssafy.interviewstudy.domain.study;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.study.StudyDtoRequest;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Study {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Integer id;

    private String title;

    @Column(length = 2000)
    private String description;

    private String appliedJob;

    @Enumerated(EnumType.STRING)
    private CareerLevel careerLevel;

    private Integer capacity;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deadline;

    @ColumnDefault("false")
    @Builder.Default
    private Boolean isRecruit = false;

    @ColumnDefault("false")
    @Builder.Default
    @Column(insertable = false)
    private Boolean isDelete = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leader_id")
    private Member leader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company appliedCompany;

    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    List<StudyChat> studyChats = new ArrayList<>();

    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    List<StudyMember> studyMembers = new ArrayList<>();

    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    List<StudyRequest> studyRequests = new ArrayList<>();

    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    List<StudyTag> studyTags = new ArrayList<>();

    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    List<StudyCalendar> studyCalendars = new ArrayList<>();

    @OneToMany(mappedBy = "study", fetch = FetchType.LAZY)
    List<StudyBookmark> studyBookmarks = new ArrayList<>();

    public Study(StudyDtoRequest studyDtoRequest){
        this.title = studyDtoRequest.getTitle();
        this.description = studyDtoRequest.getDescription();
        this.appliedJob = studyDtoRequest.getAppliedJob();
        this.careerLevel = studyDtoRequest.getCareerLevel();
        this.capacity = studyDtoRequest.getCapacity();
        this.deadline = studyDtoRequest.getDeadline();
        this.isRecruit = studyDtoRequest.getRecruitment();
    }

    public void updateLeader(Member member){
        this.leader = member;
    }

    public void updateCompany(Company appliedCompany){
        this.appliedCompany = appliedCompany;
    }

    public void deleteStudy(){
        this.isDelete = true;
    }

    public void updateStudy(StudyDtoRequest studyDtoRequest){
        this.title = studyDtoRequest.getTitle();
        this.description = studyDtoRequest.getDescription();
        this.appliedJob = studyDtoRequest.getAppliedJob();
        this.careerLevel = studyDtoRequest.getCareerLevel();
        this.capacity = studyDtoRequest.getCapacity();
        this.deadline = studyDtoRequest.getDeadline();
        this.isRecruit = studyDtoRequest.getRecruitment();
    }
}
