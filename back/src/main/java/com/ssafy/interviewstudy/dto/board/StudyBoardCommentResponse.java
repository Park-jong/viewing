package com.ssafy.interviewstudy.dto.board;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudyBoardCommentResponse {
    private Integer id;
    private Author author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer commentCount;
    private Boolean isDelete;

    private List<StudyBoardCommentReplyResponse> replies;

    @Builder
    public StudyBoardCommentResponse(Integer id, Author author, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer commentCount, Boolean isDelete, List<StudyBoardCommentReplyResponse> replies) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentCount = commentCount;
        this.isDelete = isDelete;
        this.replies = replies;
    }
}
