package com.ssafy.interviewstudy.dto.board;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import com.ssafy.interviewstudy.domain.board.BoardType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class BoardResponse {
    private Integer id;

    private Author author;
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private BoardType boardType;
    private Boolean isLike;

    private List<ArticleFile> articleFiles;

    @Builder
    public BoardResponse(Integer id, Author author, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt, Integer viewCount, Integer commentCount, Integer likeCount, BoardType boardType, Boolean isLike, List<ArticleFile> articleFiles) {
        this.id = id;
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
