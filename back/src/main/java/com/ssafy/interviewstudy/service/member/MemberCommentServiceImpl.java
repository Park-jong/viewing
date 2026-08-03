package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.repository.member.MemberCommentRepository;
import com.ssafy.interviewstudy.service.board.generalBoard.BoardDtoManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberCommentServiceImpl implements MemberCommentService {

    private final MemberCommentRepository memberCommentRepository;
    private final BoardDtoManager boardDtoManger;

    @Override
    public List<BoardResponse> getCommentedArticle(BoardRequest boardRequest, BoardType boardType) {
        return memberCommentRepository.getCommentedBoardByMemberId(boardRequest.getMemberId(), boardType)
                .stream()
                .map(boardDtoManger::fromEntityWithoutContent)
                .collect(Collectors.toList());
    }
}
