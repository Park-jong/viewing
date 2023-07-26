package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

//import java.util.List;

public interface BoardRepository<T extends Board> extends JpaRepository<T, Integer> {
    Page<T> findAll(Pageable pageable);

    // 조회수 업데이트
    @Transactional
    @Modifying
    @Query("update Board b set b.viewCount = b.viewCount + 1 where b.id = :id")
    int updateViewCount(Integer id);

    // 내가 쓴 모든 글 조회
    @Query("SELECT b FROM Board b where b.author.id = :memberId")
    Page<Board> findByMemberId(Integer memberId, Pageable pageable);

}
