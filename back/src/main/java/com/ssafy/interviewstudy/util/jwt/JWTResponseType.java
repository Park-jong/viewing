package com.ssafy.interviewstudy.util.jwt;

public enum JWTResponseType {

    //jwt 토큰이 만료되었을때
    TOKEN_EXPIRED,
    //jwt 토큰이 유효성 검증에서 실패했을때
    INVALID_TOKEN,
    //jwt 토큰이 유효함
    VALID_TOKEN

}