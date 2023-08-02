package com.ssafy.interviewstudy.dto.board;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.board.ArticleFile;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class StudyBoardResponse{
    private Integer id;
    private Integer studyId;

    private Author author;
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer commentCount;

    private List<ArticleFile> articleFiles;

    @Builder
    public StudyBoardResponse(Integer id, Integer studyId, Author author, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer commentCount, List<ArticleFile> articleFiles) {
        this.id = id;
        this.studyId = studyId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commentCount = commentCount;
        this.articleFiles = articleFiles;
    }
}
