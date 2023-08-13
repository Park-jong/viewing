package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.repository.board.BoardRepository;
import com.ssafy.interviewstudy.repository.member.MemberArticleLikeRepository;
import com.ssafy.interviewstudy.service.board.BoardDtoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberArticleService {

    private final MemberArticleLikeRepository memberArticleLikeRepository;

    private final BoardDtoService boardDtoService;

    private final BoardRepository boardRepository;


    @Transactional(readOnly = true)
    public List<BoardResponse> getLikedArticleByMemberId(BoardRequest boardRequest, BoardType boardType){
        List<BoardResponse> boardResponses = new ArrayList<>();
        List<Board> boardList = new ArrayList<>();
        boardList = memberArticleLikeRepository.getArticleByMemberId(boardRequest.getMemberId(),boardType);
        for(Board b : boardList){
            boardResponses.add(boardDtoService.fromEntityWithoutContent(b));
        }
        return boardResponses;
    }

    @Transactional(readOnly = true)
    public List<BoardResponse> getArticleByMemberId(BoardRequest boardRequest, BoardType boardType,Pageable pageable){
        List<BoardResponse> boardResponses = new ArrayList<>();
        Page<Board> boardList = boardRepository.findByMemberIdAndBoardType(boardRequest.getMemberId(),boardType,pageable);
        for(Board b : boardList){
            boardResponses.add(boardDtoService.fromEntityWithoutContent(b));
        }
        return boardResponses;
    }

}
