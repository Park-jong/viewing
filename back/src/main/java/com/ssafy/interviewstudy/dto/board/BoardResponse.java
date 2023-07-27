package com.ssafy.interviewstudy.dto.board;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import com.ssafy.interviewstudy.domain.board.Board;
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
    private String boardType;
    private Boolean isLike;

    private List<ArticleFile> articleFiles;
}
