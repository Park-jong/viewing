package com.ssafy.interviewstudy.config;

import com.ssafy.interviewstudy.util.auth.JwtMemberInfoArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ArgumentResolverConfig implements WebMvcConfigurer {

    private final JwtMemberInfoArgumentResolver jwtMemberInfoArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(jwtMemberInfoArgumentResolver);
    }
}
