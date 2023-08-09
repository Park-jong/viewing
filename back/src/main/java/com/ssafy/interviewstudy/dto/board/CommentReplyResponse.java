package com.ssafy.interviewstudy.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class CommentReplyResponse {
    private Integer commentId;
    private Author author;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime updatedAt;

    private Integer likeCount;
    private Boolean isLike;
    private Boolean isDelete;

    @Builder
    public CommentReplyResponse(Integer commentId, Author author, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer likeCount, Boolean isLike, Boolean isDelete) {
        this.commentId = commentId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likeCount = likeCount;
        this.isLike = isLike;
        this.isDelete = isDelete;
    }
}
