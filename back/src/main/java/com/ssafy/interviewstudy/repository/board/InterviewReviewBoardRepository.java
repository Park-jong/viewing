package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.InterviewReviewBoard;
import com.ssafy.interviewstudy.domain.board.StudyBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface InterviewReviewBoardRepository  extends JpaRepository<InterviewReviewBoard, Integer> {

    // 검색조건 : 제목 제목+내용 작성자
    @Query("select b from InterviewReviewBoard b where b.title like %:keyword%")
    Page<InterviewReviewBoard> findByTitleContaining(@Param("keyword") String keyword, Pageable pageable);

    // 제목+내용
    @Query("select b from InterviewReviewBoard b where b.title like %:keyword% or b.content like %:keyword%")
    Page<InterviewReviewBoard> findByTitleContainingOrContentContaining(@Param("keyword") String keyword, Pageable pageable);

    // 작성자
    @Query("SELECT b FROM InterviewReviewBoard b where b.author.nickname like %:keyword%")
    Page<InterviewReviewBoard> findWithAuthor(@Param("keyword") String keyword, Pageable pageable);

    // 내가 쓴 글 조회
    @Query("SELECT b FROM InterviewReviewBoard b where b.author.id = :memberId")
    Page<InterviewReviewBoard> findByMemberId(Integer memberId, Pageable pageable);
}
