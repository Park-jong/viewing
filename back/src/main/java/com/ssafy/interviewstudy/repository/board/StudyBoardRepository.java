package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.InterviewReviewBoard;
import com.ssafy.interviewstudy.domain.board.StudyBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyBoardRepository extends JpaRepository<StudyBoard, Integer> {

    // 검색조건 : 제목 제목+내용 작성자
    @Query("select b from StudyBoard b where b.title like %:keyword%")
    Page<StudyBoard> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);

    // 제목+내용
    @Query("select b from StudyBoard b where b.title like %:keyword% or b.content like %:keyword%")
    Page<StudyBoard> findByTitleContainingOrContentContaining(@Param("keyword") String keyword, Pageable pageable);

    // 작성자
    @Query("SELECT b FROM StudyBoard b where b.author.nickname like %:keyword%")
    Page<StudyBoard> findWithAuthor(@Param("keyword") String keyword, Pageable pageable);

    // 내가 쓴 글 조회
    @Query("SELECT b FROM StudyBoard b where b.author.id = :memberId")
    Page<StudyBoard> findByMemberId(Integer memberId, Pageable pageable);
}
