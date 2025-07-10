package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberCommentService {
    @Transactional(readOnly = true)
    List<BoardResponse> getCommentedArticle(BoardRequest boardRequest, BoardType boardType);
}
