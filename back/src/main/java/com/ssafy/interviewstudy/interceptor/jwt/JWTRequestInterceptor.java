package com.ssafy.interviewstudy.interceptor.jwt;

import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.util.JWTProvider;
import com.ssafy.interviewstudy.util.JWTProviderImpl;
import com.ssafy.interviewstudy.util.JWTResponseType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JWTRequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //JWT관리 클래스 생성
        JWTProvider jwtProvider = new JWTProviderImpl();

        //Authorization 헤더 읽기
        String authorizationHeader = request.getHeader("Authorization");

        //Authorization 헤더가 없으면 = JWT 토큰 없음
        if(authorizationHeader == null){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"No JWT Token");
            return false;
        }

        //Authorization 헤더는 JWT토큰을 담는 경우 Bearer 방식을 일반적으로 사용함
        //Authorization : Bearer {JWT토큰내용}
        String jwtTokenAuthorizationHeader = authorizationHeader.substring("Bearer ".length());

        //JWT 토큰 유효성 검사
        JWTResponseType jwtResponseType =
                jwtProvider.isValidToken(jwtTokenAuthorizationHeader);
        if(jwtResponseType == JWTResponseType.TOKEN_EXPIRED){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"JWT Token Expired");
            return false;
        }
        if(jwtResponseType == JWTResponseType.INVALID_TOKEN){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"Invalid JWT Token");
            return false;
        }

        //JWT 토큰에서 멤버정보 추출
        JWTMemberInfo jwtMemberInfo = jwtProvider
                .getJWTMemberInfo(jwtTokenAuthorizationHeader);

        request.setAttribute("JWTMemberInfo",jwtMemberInfo);
        return true;
    }
}

