package com.ssafy.interviewstudy.repository.calendar;

import com.ssafy.interviewstudy.domain.calendar.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar,Integer> {

    //일정 조회
    @Query("select c from Calendar c inner  join fetch c.author where c.author.id=:id")
    public List<Calendar> findCalendarsByAuthorId(@Param("id") Integer authorId);

    //일정 삭제
    @Modifying(clearAutomatically = true)
    Integer deleteCalendarById(Integer calendarId);

    public Calendar findCalendarByAuthorIdAndId(Integer authorId,Integer id);
}
