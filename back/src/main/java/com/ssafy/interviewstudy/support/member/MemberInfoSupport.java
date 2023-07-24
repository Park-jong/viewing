package com.ssafy.interviewstudy.support.member;

public interface MemberInfoSupport {
    public MemberInfoResult getMemberInfo(SocialLoginType socialLoginType,String accessToken);
}
