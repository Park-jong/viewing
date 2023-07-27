package com.ssafy.interviewstudy.dto.board;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Data
public class CommentResponse {
    private Integer id;
    private Author author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private Boolean isLike;

    private Boolean isDelete;

    @Builder
    public CommentResponse(Integer id, Author author, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer viewCount, Integer commentCount, Integer likeCount, Boolean isLike, Boolean isDelete) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLike = isLike;
        this.isDelete = isDelete;
    }
}
