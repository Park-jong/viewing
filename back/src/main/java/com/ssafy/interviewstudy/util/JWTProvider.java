package com.ssafy.interviewstudy.util;

import com.ssafy.interviewstudy.dto.member.JWTMemberInfo;

public interface JWTProvider {
    public JWTResponseType isValidToken(String token);
    public String provideToken(JWTMemberInfo jwtMemberInfo);

    public JWTMemberInfo getJWTMemberInfo(String token);
}
