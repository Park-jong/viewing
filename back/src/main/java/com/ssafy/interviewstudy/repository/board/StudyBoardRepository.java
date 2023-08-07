package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.board.StudyBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyBoardRepository extends JpaRepository<StudyBoard, Integer> {


    Page<StudyBoard> findByStudy_Id(Integer studyId, Pageable pageable);

    // 검색조건 : 제목 제목+내용 작성자
    @Query("select b from StudyBoard b JOIN b.author ba join b.study bs where b.title like %:keyword% and b.study.id = :studyId order by b.createdAt desc")
    Page<StudyBoard> findByTitleContaining(Integer studyId, @Param("keyword") String keyword, Pageable pageable);

    // 제목+내용
    @Query("select b from StudyBoard b JOIN b.author ba join b.study bs where b.title like %:keyword% or b.content like %:keyword% and b.study.id = :studyId order by b.createdAt desc")
    Page<StudyBoard> findByTitleOrContent(Integer studyId, @Param("keyword") String keyword, Pageable pageable);

    // 작성자
    @Query("SELECT b FROM StudyBoard b JOIN b.author ba join b.study bs where ba.nickname like %:keyword% and b.study.id = :studyId order by b.createdAt desc")
    Page<StudyBoard> findWithAuthor(Integer studyId, @Param("keyword") String keyword, Pageable pageable);

    // 내가 쓴 모든 글 조회
    @Query("SELECT b FROM StudyBoard b JOIN b.author ba join b.study bs where ba.id = :memberId and b.study.id = :studyId order by b.createdAt desc")
    Page<StudyBoard> findAllByMemberId(Integer studyId, Integer memberId, Pageable pageable);

}
