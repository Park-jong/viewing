package com.ssafy.interviewstudy.support.member;

import lombok.Builder;
import lombok.Getter;

@Getter
public class MemberInfoResult {
    private String email;
    private Boolean isSucess;
    private String memberId;
    private SocialLoginType socialLoginType;
    @Builder
    public MemberInfoResult(String email, Boolean isSucess, String memberId, SocialLoginType socialLoginType) {
        this.email = email;
        this.isSucess = isSucess;
        this.memberId = memberId;
        this.socialLoginType = socialLoginType;
    }
}
