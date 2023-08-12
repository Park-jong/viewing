package com.ssafy.interviewstudy.dto.board;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.board.ArticleFile;
import com.ssafy.interviewstudy.domain.board.BoardType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardResponse {
    private Integer articleId;

    private Author author;
    private String title;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;

    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private BoardType boardType;
    private Boolean isLike;

    private List<FileResponse> articleFiles;

    @Builder
    public BoardResponse(Integer articleId, Author author, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer viewCount, Integer commentCount, Integer likeCount, BoardType boardType, Boolean isLike, List<FileResponse> articleFiles) {
        this.articleId = articleId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.boardType = boardType;
        this.isLike = isLike;
        this.articleFiles = articleFiles;
    }
}
