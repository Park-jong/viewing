package com.ssafy.interviewstudy.dto.board;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentRequest {

    private Integer memberId;
    private Integer articleId;
    private String content;

    @Builder
    public CommentRequest(Integer memberId, Integer articleId, String content) {
        this.memberId = memberId;
        this.articleId = articleId;
        this.content = content;
    }
}
