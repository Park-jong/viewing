package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.StudyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyBookmarkRepository extends JpaRepository<StudyBookmark, Integer> {
    public StudyBookmark findStudyBookmarkByStudyIdAndMemberId(Integer studyId, Integer memberId);
}
