package com.ssafy.interviewstudy.domain.study;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Study {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "study_id")
    private Integer id;

    private String title;

    private String description;

    private String appliedJob;

    @Enumerated(EnumType.STRING)
    private CareerLevel careerLevel;

    private int capacity;

    private LocalDateTime createdAt;

    private LocalDateTime deadline;

    private boolean isRecruit;

    private boolean isDelete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member leader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company appliedCompany;

    @OneToMany(mappedBy = "study")
    List<StudyChat> studyChats = new ArrayList<>();

    @OneToMany(mappedBy = "study")
    List<StudyMember> studyMembers = new ArrayList<>();

    @OneToMany(mappedBy = "study")
    List<StudyRequest> studyRequests = new ArrayList<>();

    @OneToMany(mappedBy = "study")
    List<StudyTag> studyTags = new ArrayList<>();

    @OneToMany(mappedBy = "study")
    List<StudyCalendar> studyCalendars = new ArrayList<>();

    @OneToMany(mappedBy = "study")
    List<StudyBookmark> studyBookmarks = new ArrayList<>();

    public Study(String title, String description, String appliedJob, int capacity) {
        this.title = title;
        this.description = description;
        this.appliedJob = appliedJob;
        this.capacity = capacity;
    }
}
