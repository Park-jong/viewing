package com.ssafy.interviewstudy.interceptor.jwt;

import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.util.jwt.JWTProvider;
import com.ssafy.interviewstudy.util.jwt.JWTProviderImpl;
import com.ssafy.interviewstudy.util.jwt.JWTResponseType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JWTRequestInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {


        Boolean isJwtRequired = true;

        //자기가 처리해야 할 부분인지 체크
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 컨트롤러 메서드에 붙은 어노테이션 정보 가져오기
            JWTRequired annotation = handlerMethod.getMethodAnnotation(JWTRequired.class);
            if (annotation == null) {
                return true;
            }
            else{
                isJwtRequired = annotation.required();
            }
        }
        else return true;


        //JWT관리 클래스 생성
        JWTProvider jwtProvider = new JWTProviderImpl();

        //Authorization 헤더 읽기
        String authorizationHeader = request.getHeader("Authorization");

        //Authorization 헤더가 없으면 = JWT 토큰 없음
        if(authorizationHeader == null){
            if(!isJwtRequired) return true;
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

        request.setAttribute("memberInfo",jwtMemberInfo);
        return true;
    }
}

