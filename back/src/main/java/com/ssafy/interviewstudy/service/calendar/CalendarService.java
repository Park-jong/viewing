package com.ssafy.interviewstudy.service.calendar;

import com.ssafy.interviewstudy.dto.calendar.CalendarCreatedResponse;
import com.ssafy.interviewstudy.dto.calendar.CalendarListResponse;
import com.ssafy.interviewstudy.dto.calendar.CalendarRetrieveRequest;
import org.springframework.transaction.annotation.Transactional;

public interface CalendarService {
    //나의 일정 조회
    @Transactional
    CalendarListResponse getCalendarList(Integer memberId);

    //일정 추가
    @Transactional
    CalendarCreatedResponse createCalendar(CalendarRetrieveRequest calendarDto);

    //일정 삭제
    @Transactional
    void deleteCalendar(Integer calendarId);

    //일정 수정
    @Transactional
    void updateCalendar(CalendarRetrieveRequest calendarDto);

    //본인 일정이 맞는지 체크
    Boolean checkOwnCalendar(Integer memberId, Integer calendarId);
}
