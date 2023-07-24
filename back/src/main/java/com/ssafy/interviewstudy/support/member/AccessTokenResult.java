package com.ssafy.interviewstudy.support.member;

import lombok.Getter;

@Getter
public class AccessTokenResult {
    private String accessToken;
    private Boolean isSuccess;
    public AccessTokenResult(String accessToken, Boolean isSuccess) {
        this.accessToken = accessToken;
        this.isSuccess = isSuccess;
    }
}
