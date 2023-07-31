package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.repository.member.MemberArticleLikeRepository;
import com.ssafy.interviewstudy.service.board.BoardDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberArticleLikeService {

    private MemberArticleLikeRepository memberArticleLikeRepository;

    private BoardDtoService boardDtoService;

    @Autowired
    public MemberArticleLikeService(MemberArticleLikeRepository memberArticleLikeRepository, BoardDtoService boardDtoService) {
        this.memberArticleLikeRepository = memberArticleLikeRepository;
        this.boardDtoService = boardDtoService;
    }


    @Transactional
    public List<BoardResponse> getLikedArticleByMemberId(BoardRequest boardRequest, BoardType boardType){
        List<BoardResponse> boardResponses = new ArrayList<>();
        List<Board> boardList = new ArrayList<>();
        boardList = memberArticleLikeRepository.getArticleByMemberId(boardRequest.getMemberId(),boardType.toString());
        for(Board b : boardList){
            boardResponses.add(boardDtoService.fromEntityWithoutContent(b));
        }
        return boardResponses;
    }

}
