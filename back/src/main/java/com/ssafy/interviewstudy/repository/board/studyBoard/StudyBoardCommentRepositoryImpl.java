package com.ssafy.interviewstudy.repository.board.studyBoard;

import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.board.StudyBoardComment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyBoardCommentRepositoryImpl implements StudyBoardCommentRepository {

    private final JPAStudyBoardCommentRepository jpaStudyBoardCommentRepository;

    @Override
    public Optional<StudyBoardComment> findById(Integer id) {
        return jpaStudyBoardCommentRepository.findById(id);
    }

    @Override
    public StudyBoardComment save(StudyBoardComment studyBoardComment) {
        return jpaStudyBoardCommentRepository.save(studyBoardComment);
    }

    @Override
    public void delete(StudyBoardComment studyBoardComment) {
        jpaStudyBoardCommentRepository.delete(studyBoardComment);
    }

    @Override
    public void deleteById(Integer id) {
        jpaStudyBoardCommentRepository.deleteById(id);
    }

    @Override
    public List<StudyBoardComment> findAllByArticle(StudyBoard article) {
        return jpaStudyBoardCommentRepository.findAllByArticle(article);
    }

    @Override
    public Integer countByArticle(StudyBoard article) {
        return jpaStudyBoardCommentRepository.countByArticle(article);
    }

    @Override
    public Integer countByComment(Integer commentId) {
        return jpaStudyBoardCommentRepository.countByComment(commentId);
    }


}
