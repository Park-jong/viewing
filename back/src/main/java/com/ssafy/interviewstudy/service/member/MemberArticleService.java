package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MemberArticleService {
    @Transactional(readOnly = true)
    List<BoardResponse> getLikedArticleByMemberId(BoardRequest boardRequest, BoardType boardType);

    @Transactional(readOnly = true)
    List<BoardResponse> getArticleByMemberId(BoardRequest boardRequest, BoardType boardType, Pageable pageable);
}
