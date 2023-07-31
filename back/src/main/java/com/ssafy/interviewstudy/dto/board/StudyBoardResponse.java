package com.ssafy.interviewstudy.dto.board;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class StudyBoardResponse{
    private Integer studyId;
    private Integer id;

    private Author author;
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer viewCount;
    private Integer commentCount;
    private String boardType;

    private List<ArticleFile> articleFiles;

    @Builder
    public StudyBoardResponse(Integer studyId, Integer id, Author author, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer viewCount, Integer commentCount, String boardType, List<ArticleFile> articleFiles) {
        this.studyId = studyId;
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.boardType = boardType;
        this.articleFiles = articleFiles;
    }
}
