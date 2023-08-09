package com.ssafy.interviewstudy.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Integer commentId;
    private Author author;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime updatedAt;

    private Integer commentCount;
    private Boolean isDelete;

    private List<StudyBoardCommentReplyResponse> replies;

    @Builder
    public StudyBoardCommentResponse(Integer commentId, Author author, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer commentCount, Boolean isDelete, List<StudyBoardCommentReplyResponse> replies) {
        this.commentId = commentId;
        this.author = author;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentCount = commentCount;
        this.isDelete = isDelete;
        this.replies = replies;
    }
}
