package com.ssafy.interviewstudy.config;

import com.ssafy.interviewstudy.interceptor.auth.*;
import com.ssafy.interviewstudy.interceptor.jwt.JWTRequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    private final JWTRequestInterceptor jwtRequestInterceptor;

    private final MemberInterceptor memberInterceptor;

    private final LeaderInterceptor leaderInterceptor;

    private final MemberCalendarInterceptor memberCalendarInterceptor;

    private final MemberMessageInterceptor memberMessageInterceptor;

    private final MemberNotificationInterceptor memberNotificationInterceptor;

    private final MemberStudyArticleInterceptor memberStudyArticleInterceptor;

    private final MemberStudyBookmarkInterceptor memberStudyBookmarkInterceptor;

    private final MemberStudyCommentInterceptor memberStudyCommentInterceptor;

    private final MemberStudyInterceptor memberStudyInterceptor;

    private final MemberStudyRequestInterceptor memberStudyRequestInterceptor;

    private final MemberBoardInterceptor memberBoardInterceptor;

    private final MemberArticleLikeInterceptor memberArticleLikeInterceptor;

    private final MemberCommentInterceptor memberCommentInterceptor;

    private final MemberCommentLikeInterceptor memberCommentLikeInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //jwt 인터셉터는 무조건 1번
        registry.addInterceptor(jwtRequestInterceptor).addPathPatterns("/**");

        //멤바 인타셉타
        registry.addInterceptor(memberInterceptor).addPathPatterns("/**");

        //스터디장 인터셉터
        registry.addInterceptor(leaderInterceptor).addPathPatterns("/**");

        //멤버 게시글  인터셉터
        registry.addInterceptor(memberBoardInterceptor).addPathPatterns("/**");

        //멤버 댓글 인터셉터
        registry.addInterceptor(memberCommentInterceptor).addPathPatterns("/**");

        //멤버 게시글 좋아요 인터셉터
        registry.addInterceptor(memberArticleLikeInterceptor).addPathPatterns("/**");

        //멤버 댓글 좋아요 인터셉터
        registry.addInterceptor(memberCommentLikeInterceptor).addPathPatterns("/**");

        //멤버 일정 인터셉터
        registry.addInterceptor(memberCalendarInterceptor).addPathPatterns("/**");

        //멤버 알림 인터셉터
        registry.addInterceptor(memberNotificationInterceptor).addPathPatterns("/**");

        //멤버 스터디 게시글 인터셉터
        registry.addInterceptor(memberStudyArticleInterceptor).addPathPatterns("/**");

        //멤버 스터디 북마크 인터셉터
        registry.addInterceptor(memberStudyBookmarkInterceptor).addPathPatterns("/**");

        //멤버 스터디 댓글 인터셉터
        registry.addInterceptor(memberStudyCommentInterceptor).addPathPatterns("/**");

        //멤버 스터디 인터셉터
        registry.addInterceptor(memberStudyInterceptor).addPathPatterns("/**");

        //멤버 스터디 요청 인터셉터
        registry.addInterceptor(memberStudyRequestInterceptor).addPathPatterns("/**");

        //멤버 쪽지 인터셉터
        registry.addInterceptor(memberMessageInterceptor).addPathPatterns("/**");

    }
}
