package com.ssafy.interviewstudy.interceptor.auth;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.service.member.MemberService;
import com.ssafy.interviewstudy.util.PathVariableExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class MemberInterceptor implements HandlerInterceptor {

    private final MemberService memberService;

    @Autowired
    public MemberInterceptor(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //자기가 처리해야 할 부분인지 체크
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 컨트롤러 메서드에 붙은 어노테이션 정보 가져오기
            Authority annotation = handlerMethod.getMethodAnnotation(Authority.class);
            if (annotation != null) {
                if(annotation.authorityType()!=AuthorityType.Member) return true;
            }
        }
        //uri에서 Path Variable 추출하기
        String requestUri = request.getRequestURI();
        List<Integer> pathVariables = PathVariableExtractor.extract(requestUri);

        //jwt에 담긴 유저 정보 확인
        Object jwtMemberInfoAttribute = request.getAttribute("JWTMemberInfo");
        JWTMemberInfo jwtMemberInfo;
        
        if(jwtMemberInfoAttribute instanceof JWTMemberInfo){
            jwtMemberInfo = (JWTMemberInfo) jwtMemberInfoAttribute;
        }
        else{
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"잘못된 JWT 정보");
            return false;
        }
        
        //PathVariable 유효성 검사
        if(pathVariables.size()!=1){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"잘못된 Path Variable");
            return false;
        }
        int memberId = pathVariables.get(0);
        
        //PathVariable로 멤버 조회
        Member member = memberService.findMemberByMemberId(memberId);
        if(member==null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"없는 유저 입니다.");
            return false;
        }
        else{
            if(jwtMemberInfo.getMemberId()!=member.getId()){
                response.sendError(HttpServletResponse.SC_BAD_REQUEST,"잘못된 Path Variable");
                return false;
            }
        }
        return true;
    }
}
