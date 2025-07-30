package com.ssafy.interviewstudy.repository.board.studyBoard;

import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.repository.CommonRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

public interface StudyBoardRepository extends CommonRepository<StudyBoard, Integer> {

    Page<StudyBoard> findByStudyId(Integer studyId, Pageable pageable);

    Page<StudyBoard> findByTitleContaining(Integer studyId, @Param("keyword") String keyword, Pageable pageable);

    Page<StudyBoard> findByTitleOrContent(Integer studyId, @Param("keyword") String keyword, Pageable pageable);

    Page<StudyBoard> findWithAuthor(Integer studyId, @Param("keyword") String keyword, Pageable pageable);

    Page<StudyBoard> findAllByMemberId(Integer studyId, Integer memberId, Pageable pageable);

}
