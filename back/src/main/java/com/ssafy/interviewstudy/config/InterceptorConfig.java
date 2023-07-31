package com.ssafy.interviewstudy.config;

import com.querydsl.core.annotations.Config;
import com.ssafy.interviewstudy.interceptor.auth.LeaderInterceptor;
import com.ssafy.interviewstudy.interceptor.auth.MemberInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Config
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        //jwt 인터셉터는 무조건 1번
        registry.addInterceptor(LeaderInterceptor)
                .addPathPatterns("*");
    }
}
