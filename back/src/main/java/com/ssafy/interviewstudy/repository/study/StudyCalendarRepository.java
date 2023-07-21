package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyCalendarRepository extends JpaRepository<StudyCalendar, Integer> {
    public List<StudyCalendar> findStudyCalendersByStudy(Study study);
}
