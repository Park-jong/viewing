package com.ssafy.interviewstudy.util.jwt;

import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;

public interface JWTProvider {
    public JWTResponseType isValidToken(String token);
    public String provideToken(JWTMemberInfo jwtMemberInfo);

    public JWTMemberInfo getJWTMemberInfo(String token);
}
