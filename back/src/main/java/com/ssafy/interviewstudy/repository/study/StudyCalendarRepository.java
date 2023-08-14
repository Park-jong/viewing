package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.calendar.Calendar;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyCalendar;
import com.ssafy.interviewstudy.domain.study.StudyMember;
import com.ssafy.interviewstudy.dto.calendar.CalendarListResponse;
import com.ssafy.interviewstudy.dto.calendar.CalendarRetrieveResponse;
import com.ssafy.interviewstudy.dto.study.StudyCalendarDtoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyCalendarRepository extends JpaRepository<StudyCalendar, Integer> {
    @Query("select new com.ssafy.interviewstudy.dto.study.StudyCalendarDtoResponse(sc) from StudyCalendar sc join sc.author where sc.study = :study")
    public List<StudyCalendarDtoResponse> findStudyCalendersByStudy(@Param("study") Study study);

    @Query("select new com.ssafy.interviewstudy.dto.study.StudyCalendarDtoResponse(sc) from StudyCalendar sc join sc.author where sc.id = :id")
    public StudyCalendarDtoResponse findStudyCalenderById(@Param("id") Integer id);

    //스터디 멤버 스케줄 가져오기
    @Query("select c from StudyMember sm join sm.member m join m.calendarList c where sm.study.id = :studyId")
    public List<Calendar> findMemberCalendarByStudyId(@Param("studyId") Integer studyId);

    //멤버Id로 스케줄 가져오기
    @Query("select new com.ssafy.interviewstudy.dto.study.StudyCalendarDtoResponse(sc) from StudyCalendar sc join sc.author where sc.author.id = :memberId")
    public List<StudyCalendarDtoResponse> findStudyCalendersByMemberId(@Param("memberId") Integer memberId);

    //멤버Id로 스케줄 가져오되 특정 스터디ID 제외
    @Query("select new com.ssafy.interviewstudy.dto.study.StudyCalendarDtoResponse(sc) from StudyCalendar sc join sc.author where sc.author.id = :memberId and sc.study.id != :studyId")
    public List<StudyCalendarDtoResponse> findStudyCalendersByMemberIdAndStudyId(@Param("memberId") Integer memberId, @Param("studyId") Integer studyId);
}
