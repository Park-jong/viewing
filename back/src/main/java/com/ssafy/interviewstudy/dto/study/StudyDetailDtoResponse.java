package com.ssafy.interviewstudy.dto.study;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyMember;
import com.ssafy.interviewstudy.domain.study.StudyTag;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
@Data
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class StudyDetailDtoResponse {
    private Integer studyId;

    private String title;

    private String description;

    private String appliedCompany;

    private String appliedJob;

    private Integer capacity;

    private int headCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime deadline;

    private Boolean recruitment;

    private Boolean bookmark;

    private Leader leader;

    private CareerLevel careerLevel;

    private List<String> tags = new ArrayList<>();

    private List<StudyMemberDto> members = new ArrayList<>();

    public StudyDetailDtoResponse(Study study){
        this.studyId = study.getId();
        this.title = study.getTitle();
        this.description = study.getDescription();
        this.appliedCompany = study.getAppliedCompany().getName();
        this.appliedJob = study.getAppliedJob();
        this.capacity = study.getCapacity();
        this.createdAt = study.getCreatedAt();
        this.deadline = study.getDeadline();
        this.recruitment = study.getIsRecruit();
        this.leader = new Leader(study.getLeader());
        this.careerLevel = study.getCareerLevel();
        for(StudyMember member : study.getStudyMembers()){
            this.members.add(new StudyMemberDto(member.getMember()));
        }
        List<StudyTag> tags = study.getStudyTags();
        for (StudyTag tag : tags) {
            this.tags.add(tag.getTag().getTagName());
        }
        this.headCount = this.members.size();
    }
}
