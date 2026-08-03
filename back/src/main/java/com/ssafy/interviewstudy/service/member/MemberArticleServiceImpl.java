package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.repository.board.generalBoard.BoardRepository;
import com.ssafy.interviewstudy.repository.member.MemberArticleLikeRepository;
import com.ssafy.interviewstudy.service.board.generalBoard.BoardDtoManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberArticleServiceImpl implements MemberArticleService {

    private final MemberArticleLikeRepository memberArticleLikeRepository;
    private final BoardRepository boardRepository;
    private final BoardDtoManager boardDtoManger;

    @Override
    public List<BoardResponse> getLikedArticleByMemberId(BoardRequest boardRequest, BoardType boardType) {
        return memberArticleLikeRepository.getArticleByMemberId(boardRequest.getMemberId(), boardType)
                .stream()
                .map(boardDtoManger::fromEntityWithoutContent)
                .collect(Collectors.toList());
    }

    @Override
    public List<BoardResponse> getArticleByMemberId(BoardRequest boardRequest, BoardType boardType, Pageable pageable) {
        return boardRepository.findByMemberIdAndBoardType(boardRequest.getMemberId(), boardType, pageable)
                .stream()
                .map(boardDtoManger::fromEntityWithoutContent)
                .collect(Collectors.toList());
    }

}
