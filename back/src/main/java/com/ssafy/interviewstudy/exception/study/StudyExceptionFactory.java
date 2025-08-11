package com.ssafy.interviewstudy.exception.study;


public class StudyExceptionFactory {
    public static NotFoundException studyNotFound() {
        return new NotFoundException("스터디가 존재하지 않습니다.");
    }

    public static NotFoundException studyCalendarNotFound() {
        return new NotFoundException("스터디일정이 존재하지 않습니다.");
    }

    public static NotFoundException studyMemberNotFound() {
        return new NotFoundException("스터디원이 존재하지 않습니다.");
    }
}
