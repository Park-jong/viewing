package com.ssafy.interviewstudy.dto.member.bookmark;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudyBookmarkResponse {
    @NotNull
    private Integer studyBookmarkId;

    public StudyBookmarkResponse(Integer studyBookmarkId) {
        this.studyBookmarkId = studyBookmarkId;
    }
}
