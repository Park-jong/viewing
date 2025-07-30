package com.ssafy.interviewstudy.repository.board.studyBoard;

import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.board.StudyBoardComment;
import com.ssafy.interviewstudy.repository.CommonRepository;

import java.util.List;

public interface StudyBoardCommentRepository extends CommonRepository<StudyBoardComment, Integer> {

    List<StudyBoardComment> findAllByArticle(StudyBoard article);

    Integer countByArticle(StudyBoard article);

    Integer countByComment(Integer commentId);
}
