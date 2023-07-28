package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyTagRepository extends JpaRepository<StudyTag, Integer> {
    //수정 시 기존 값 제거
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from StudyTag st where st.study = :study")
    public void deleteStudyTagByStudy(@Param("study") Study study);
}
