package com.ssafy.interviewstudy.dto.study;

import com.ssafy.interviewstudy.domain.study.Company;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyTag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
public class StudyResponse {
    private Integer studyId;

    private String title;

    private String description;

    private String appliedCompany;

    private String appliedJob;

    private int capacity;

    private int headCount;

    private LocalDateTime createdAt;

    private LocalDateTime deadline;

    private boolean isRecruit;

    private Leader leader;

    private List<String> tags = new ArrayList<>();

    public StudyResponse(Study study){
        this.studyId = study.getId();
        this.title = study.getTitle();
        this.description = study.getDescription();
        this.appliedCompany = study.getAppliedCompany().getName();
        this.appliedJob = study.getAppliedJob();
        this.capacity = study.getCapacity();
        this.createdAt = study.getCreatedAt();
        this.deadline = study.getDeadline();
        this.isRecruit = study.getIsRecruit();
        this.leader = new Leader(study.getLeader());
        List<StudyTag> tags = study.getStudyTags();
        for (StudyTag tag : tags) {
            this.tags.add(tag.getTag().getTagName());
        }
    }

    public void headCounting(int count){
        this.headCount = count;
    }
}
