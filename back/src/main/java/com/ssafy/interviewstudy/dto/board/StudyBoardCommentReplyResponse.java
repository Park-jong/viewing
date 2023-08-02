package com.ssafy.interviewstudy.dto.board;

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
    private Integer id;
    private Author author;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Boolean isDelete;

    @Builder
    public StudyBoardCommentReplyResponse(Integer id, Author author, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Boolean isDelete) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDelete = isDelete;
    }
}
