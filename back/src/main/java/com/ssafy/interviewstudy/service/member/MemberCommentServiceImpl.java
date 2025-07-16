package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.repository.member.MemberCommentRepository;
import com.ssafy.interviewstudy.service.board.BoardDtoManger;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberCommentServiceImpl implements MemberCommentService {

    private final MemberCommentRepository memberCommentRepository;

    private final BoardDtoManger boardDtoManger;

    @Transactional(readOnly = true)
    @Override
    public List<BoardResponse> getCommentedArticle(BoardRequest boardRequest, BoardType boardType) {
        List<BoardResponse> boardResponses = new ArrayList<>();
        List<Board> boardList = new ArrayList<>();
        boardList = memberCommentRepository.getCommentedBoardByMemberId(boardRequest.getMemberId(), boardType);
        for (Board b : boardList) {
            boardResponses.add(boardDtoManger.fromEntityWithoutContent(b));
        }
        return boardResponses;
    }
}
