package com.ssafy.interviewstudy.dto.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JWTMemberInfo {
    private Integer memberId;
    private String email;

    @Builder
    public JWTMemberInfo(Integer memberId, String email) {
        this.memberId = memberId;
        this.email = email;
    }
}