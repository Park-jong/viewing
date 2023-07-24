package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//import java.util.List;

public interface BoardRepository<T extends Board> extends JpaRepository<T, Integer> {
    Page<T> findAll(Pageable pageable);

//
//    // 검색조건 : 제목 제목+내용 작성자
//    @Query("select b from Board b where b.title like %:keyword% and b.board_type = :boardType")
//    Page<Board> findByTitleContaining(@Param("keyword") String keyword, String boardType, Pageable pageable);
//
//    // 제목+내용
//    @Query("select b from Board b where b.title like %:keyword% or b.content like %:keyword%")
//    Page<Board> findByTitleOrContent(@Param("keyword") String keyword, Pageable pageable);
//
//    // 작성자
//    @Query("SELECT b FROM Board b where b.author.nickname like %:keyword%")
//    Page<Board> findWithAuthor(@Param("keyword") String keyword, Pageable pageable);
//
//    // 내가 쓴 글 조회
//    @Query("SELECT b FROM Board b where b.author.id = :memberId")
//    Page<Board> findByMemberId(Integer memberId, Pageable pageable);

}
