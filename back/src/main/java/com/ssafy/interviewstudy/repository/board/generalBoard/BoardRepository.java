package com.ssafy.interviewstudy.repository.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.repository.CommonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends CommonRepository<Board, Integer> {
    Page<Board> findAll(Pageable pageable);

    int updateViewCount(Integer id);

    Page<Board> findByTitleContaining(@Param("keyword") String keyword, @Param("boardType") BoardType boardType, Pageable pageable);

    Page<Board> findByTitleOrContent(@Param("keyword") String keyword, BoardType boardType, Pageable pageable);

    Page<Board> findWithAuthor(@Param("keyword") String keyword, BoardType boardType, Pageable pageable);

    Page<Board> findAllByMemberId(Integer memberId, Pageable pageable);

    Page<Board> findByMemberIdAndBoardType(Integer memberId, BoardType boardType, Pageable pageable);

    Page<Board> findByType(BoardType boardType, Pageable pageable);

    List<Board> findAllByMember(Member member);
}
