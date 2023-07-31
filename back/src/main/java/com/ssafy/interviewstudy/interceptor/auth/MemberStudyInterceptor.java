package com.ssafy.interviewstudy.interceptor.auth;


import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.service.member.MemberService;
import com.ssafy.interviewstudy.service.study.StudyService;
import com.ssafy.interviewstudy.util.auth.PathVariableExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class MemberStudyInterceptor implements HandlerInterceptor {

    private final StudyService studyService;

    private final MemberService memberService;

    @Autowired
    public MemberStudyInterceptor(StudyService studyService, MemberService memberService) {
        this.studyService = studyService;
        this.memberService = memberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Boolean isStudyMember = false;

        //자기가 처리해야 할 부분인지 체크
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 컨트롤러 메서드에 붙은 어노테이션 정보 가져오기
            Authority annotation = handlerMethod.getMethodAnnotation(Authority.class);
            if (annotation != null) {
                if(annotation.authorityType()!= AuthorityType.Member_Study
                        && annotation.authorityType()!=AuthorityType.Study_Member) return true;
                if(annotation.authorityType() == AuthorityType.Study_Member) isStudyMember = true;
            }
            else return true;
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
        if(pathVariables.size()!=2){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"잘못된 Path Variable");
            return false;
        }
        Integer memberId = pathVariables.get(0);
        Integer studyId = pathVariables.get(1);

        if(isStudyMember){
            memberId = pathVariables.get(1);
            studyId = pathVariables.get(0);
        }

        //스터디에 가입한 멤버 조회
        Boolean isMemberInStudy = studyService.checkStudyMember(studyId,memberId);
        
        if(!isMemberInStudy){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"해당 스터디의 스터디원이 아닙니다");
            return false;
        }
        return true;
    }
}
