package com.ssafy.interviewstudy.dto.board;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentRequest {

    private Integer memberId;
    private Integer articleId;
    private String content;
}
