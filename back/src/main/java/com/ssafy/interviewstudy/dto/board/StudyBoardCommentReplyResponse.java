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
public class StudyBoardCommentReplyResponse {
    private Integer commentId;
    private Author author;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;

    private Boolean isDelete;

    @Builder
    public StudyBoardCommentReplyResponse(Integer commentId, Author author, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDelete) {
        this.commentId = commentId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDelete = isDelete;
    }
}
