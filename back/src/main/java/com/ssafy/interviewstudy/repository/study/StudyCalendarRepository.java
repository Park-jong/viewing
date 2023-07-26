package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyCalendar;
import com.ssafy.interviewstudy.dto.study.StudyCalendarDtoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyCalendarRepository extends JpaRepository<StudyCalendar, Integer> {
    @Query("select new com.ssafy.interviewstudy.dto.study.StudyCalendarDtoResponse(sc) from StudyCalendar sc join sc.author where sc.study = :study")
    public List<StudyCalendarDtoResponse> findStudyCalendersByStudy(@Param("study") Study study);
}
