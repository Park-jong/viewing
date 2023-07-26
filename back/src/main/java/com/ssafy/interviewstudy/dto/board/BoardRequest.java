package com.ssafy.interviewstudy.dto.board;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class BoardRequest {

    private Integer memberId;
    private Integer id;

    private String title;
    private String content;

    private List<ArticleFile> files;

    private static Board toEntity(BoardRequest boardRequest, Member author) {
        return Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .author(author)
                .build();
    }
}
