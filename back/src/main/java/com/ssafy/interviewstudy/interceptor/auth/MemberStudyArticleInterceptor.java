package com.ssafy.interviewstudy.interceptor.auth;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.service.board.studyBoard.StudyBoardService;
import com.ssafy.interviewstudy.service.member.MemberService;
import com.ssafy.interviewstudy.service.study.StudyService;
import com.ssafy.interviewstudy.util.auth.PathVariableExtractor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class MemberStudyArticleInterceptor implements HandlerInterceptor {

    private final StudyService studyService;

    private final MemberService memberService;

    private final StudyBoardService studyBoardService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //자기가 처리해야 할 부분인지 체크
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 컨트롤러 메서드에 붙은 어노테이션 정보 가져오기
            Authority annotation = handlerMethod.getMethodAnnotation(Authority.class);
            if (annotation != null) {
                if(annotation.authorityType() != AuthorityType.Member_Study_Article) return true;
            }
            else return true;
        }
        else return true;

        //uri에서 Path Variable 추출하기
        String requestUri = request.getRequestURI();
        List<Integer> pathVariables = PathVariableExtractor.extract(requestUri);

        //jwt에 담긴 유저 정보 확인
        Object jwtMemberInfoAttribute = request.getAttribute("memberInfo");
        JWTMemberInfo jwtMemberInfo;

        if(jwtMemberInfoAttribute instanceof JWTMemberInfo){
            jwtMemberInfo = (JWTMemberInfo) jwtMemberInfoAttribute;
        }
        else{
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,"잘못된 JWT 정보");
            return false;
        }

        Member member = memberService.findMemberByMemberId(jwtMemberInfo.getMemberId());

        if(member==null){
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"없는 유저 입니다.");
            return false;
        }
        Integer memberId,studyId,articleId;
        if(pathVariables.size()==2){
            memberId = member.getId();
            studyId = pathVariables.get(0);
            articleId = pathVariables.get(1);
        }
        else{
            response.sendError(HttpServletResponse.SC_BAD_REQUEST,"잘못된 Path Variable");
            return false;
        }

        if(!studyService.checkStudyMember(studyId,memberId)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"해당 스터디원이 아닙니다.");
            return false;
        }

        if(request.getMethod().equals("GET")){
            return true;
        }

        if(!studyBoardService.checkAuthor(articleId,memberId)){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,"해당 게시글 작성자가 아닙니다");
            return false;
        }

        return true;

    }
}
