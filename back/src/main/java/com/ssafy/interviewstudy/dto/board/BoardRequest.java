package com.ssafy.interviewstudy.dto.board;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ssafy.interviewstudy.domain.board.ArticleFile;
import com.ssafy.interviewstudy.domain.board.BoardType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BoardRequest {

    private Integer memberId;

    // 그냥 게시판일 때는 null
    private Integer studyId;

    private String title;
    private String content;
    private BoardType boardType;
    private List<FileResponse> filesDeleted;

    @Builder
    public BoardRequest(Integer memberId, Integer studyId, String title, String content, BoardType boardType, List<FileResponse> filesDeleted) {
        this.memberId = memberId;
        this.studyId = studyId;
        this.title = title;
        this.content = content;
        this.boardType = boardType;
        this.filesDeleted = filesDeleted;
    }
}
