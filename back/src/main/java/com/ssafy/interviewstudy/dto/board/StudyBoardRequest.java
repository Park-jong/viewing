package com.ssafy.interviewstudy.dto.board;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class StudyBoardRequest {

    private Integer memberId;
    private Integer studyId;

    private String title;
    private String content;
    private List<ArticleFile> files;

}
