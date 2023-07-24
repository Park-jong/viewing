package com.ssafy.interviewstudy.support.member;

import lombok.Getter;

@Getter
public class MemberInfoResult {
    private String email;
    private Boolean isSucess;

    public MemberInfoResult(String email, Boolean isSucess) {
        this.email = email;
        this.isSucess = isSucess;
    }
}
