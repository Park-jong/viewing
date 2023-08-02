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
public class CommentResponse {
    private Integer id;
    private Author author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer commentCount;
    private Integer likeCount;
    private Boolean isLike;
    private Boolean isDelete;

    private List<CommentReplyResponse> replies;

    @Builder
    public CommentResponse(Integer id, Author author, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer commentCount, Integer likeCount, Boolean isLike, Boolean isDelete, List<CommentReplyResponse> replies) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLike = isLike;
        this.isDelete = isDelete;
        this.replies = replies;
    }
}
