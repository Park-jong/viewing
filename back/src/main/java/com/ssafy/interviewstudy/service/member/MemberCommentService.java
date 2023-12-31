package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.repository.member.MemberCommentRepository;
import com.ssafy.interviewstudy.service.board.BoardDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MemberCommentService {

    private MemberCommentRepository memberCommentRepository;

    private BoardDtoService boardDtoService;
    @Autowired
    public MemberCommentService(MemberCommentRepository memberCommentRepository, BoardDtoService boardDtoService) {
        this.memberCommentRepository = memberCommentRepository;
        this.boardDtoService = boardDtoService;
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getCommentedArticle(BoardRequest boardRequest, BoardType boardType){
        List<BoardResponse> boardResponses = new ArrayList<>();
        List<Board> boardList = new ArrayList<>();
        boardList = memberCommentRepository.getCommentedBoardByMemberId(boardRequest.getMemberId(),boardType);
        for(Board b : boardList){
            boardResponses.add(boardDtoService.fromEntityWithoutContent(b));
        }
        return boardResponses;
    }
}
