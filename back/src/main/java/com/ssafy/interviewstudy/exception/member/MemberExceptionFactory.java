package com.ssafy.interviewstudy.exception.member;

import com.ssafy.interviewstudy.exception.message.NotFoundException;

public class MemberExceptionFactory {
    public static NotFoundException memberNotFound() {
        return new NotFoundException("회원이 존재하지 않습니다.");
    }
}
