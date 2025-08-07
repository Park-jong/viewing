package com.ssafy.interviewstudy.service.study.studyCalendar;

import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.dto.study.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyCalendarService {
    //스터디 일정 조회
    List<Object> findStudyCalendarsByStudy(Integer studyId);

    List<Object> findStudyCalendarsByMemberId(Integer memberId);

    List<Object> findStudyCalendarsByMemberIdStudyId(Integer memberId, Integer studyId);

    //일정 개별 조회
    StudyCalendarDtoResponse findStudyCalendarByStudy(Integer studyId, Integer calendarId);

    //스터디 일정 추가
    Integer addStudyCalendar(Integer studyId, StudyCalendarDtoRequest studyCalendarDtoRequest);

    //스터디 일정 수정
    void modifyStudyCalendar(Integer studyId, Integer studyCalendarId, StudyCalendarDtoRequest studyCalendarDtoRequest);

    //스터디 일정 삭제
    void removeStudyCalendar(Integer studyId, Integer calendarId);
}
