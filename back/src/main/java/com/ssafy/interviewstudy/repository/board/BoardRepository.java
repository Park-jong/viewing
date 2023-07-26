package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BoardRepository<T extends Board> extends JpaRepository<T, Integer> {
    Page<T> findAll(Pageable pageable);

    // 조회수 업데이트
    @Transactional
    @Modifying
    @Query("update Board b set b.viewCount = b.viewCount + 1 where b.id = :id")
    int updateViewCount(Integer id);

    // 내가 쓴 모든 글 조회
    @Query("SELECT b FROM Board b where b.author.id = :memberId")
    Page<Board> findAllByMemberId(Integer memberId, Pageable pageable);

    // 게시글과 작성자 정보를 함께 조회하는 쿼리
    @Query("select b from Board b JOIN FETCH b.author where Type(b) = :boardType")
    List<Board> findAllWithAuthor(String boardType);

    // 검색조건 : 제목 제목+내용 작성자
    @Query("select b from Board b where b.title like %:keyword% and Type(b) = :boardType")
    Page<Board> findByTitleContaining(@Param("keyword") String keyword, String boardType, Pageable pageable);

    // 제목+내용
    @Query("select b from Board b where b.title like %:keyword% or b.content like %:keyword% and Type(b) = :boardType")
    Page<Board> findByTitleOrContent(@Param("keyword") String keyword, String boardType, Pageable pageable);

    // 작성자
    @Query("SELECT b FROM Board b where b.author.nickname like %:keyword% and Type(b) = :boardType")
    Page<Board> findWithAuthor(@Param("keyword") String keyword, String boardType, Pageable pageable);

    // 내가 쓴 글 조회(게시판 별로)
    @Query("select b from Board b where b.author.id = :memberId and Type(b) = :boardType")
    Page<Board> findByMemberId(Integer memberId, String boardType, Pageable pageable);

    // 글 리스트 조회
    @Query("select b from Board b where Type(b) = :boardType")
    Page<Board> findByType(String boardType, Pageable pageable);
}
