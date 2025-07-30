package com.ssafy.interviewstudy.repository.board.studyBoard;

import com.ssafy.interviewstudy.domain.board.StudyBoard;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class StudyBoardRepositoryImpl implements StudyBoardRepository {

    private final JPAStudyBoardRepository jpaStudyBoardRepository;

    @Override
    public Optional<StudyBoard> findById(Integer id) {
        return jpaStudyBoardRepository.findById(id);
    }

    @Override
    public StudyBoard save(StudyBoard studyBoard) {
        return jpaStudyBoardRepository.save(studyBoard);
    }

    @Override
    public void delete(StudyBoard studyBoard) {
        jpaStudyBoardRepository.delete(studyBoard);
    }

    @Override
    public void deleteById(Integer id) {
        jpaStudyBoardRepository.deleteById(id);
    }

    @Override
    public Page<StudyBoard> findByStudyId(Integer studyId, Pageable pageable) {
        return jpaStudyBoardRepository.findByStudyId(studyId, pageable);
    }

    @Override
    public Page<StudyBoard> findByTitleContaining(Integer studyId, String keyword, Pageable pageable) {
        return jpaStudyBoardRepository.findByTitleContaining(studyId, keyword, pageable);
    }

    @Override
    public Page<StudyBoard> findByTitleOrContent(Integer studyId, String keyword, Pageable pageable) {
        return jpaStudyBoardRepository.findByTitleOrContent(studyId, keyword, pageable);
    }

    @Override
    public Page<StudyBoard> findWithAuthor(Integer studyId, String keyword, Pageable pageable) {
        return jpaStudyBoardRepository.findWithAuthor(studyId, keyword, pageable);
    }

    @Override
    public Page<StudyBoard> findAllByMemberId(Integer studyId, Integer memberId, Pageable pageable) {
        return jpaStudyBoardRepository.findAllByMemberId(studyId, memberId, pageable);
    }
}
