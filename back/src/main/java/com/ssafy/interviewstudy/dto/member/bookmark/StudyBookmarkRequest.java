package com.ssafy.interviewstudy.dto.member.bookmark;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.study.Study;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotNull;

//북마크 생성/삭제에 사용되는 DTO
@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudyBookmarkRequest {
    //북마크한 유저 아이디
    @NotNull
    private Integer memberId;

    //북마크한 스터디 아이디
    @NotNull
    private Integer studyId;

    @Builder
    public StudyBookmarkRequest(Integer memberId, Integer studyId) {
        this.memberId = memberId;
        this.studyId = studyId;
    }
}
