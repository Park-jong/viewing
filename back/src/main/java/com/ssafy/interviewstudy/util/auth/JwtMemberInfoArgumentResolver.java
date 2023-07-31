package com.ssafy.interviewstudy.util.auth;

import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Component
public class JwtMemberInfoArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpServletRequest httpServletRequest;

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            boolean isMemberInfoAnnotation = parameter.getParameterAnnotation(MemberInfo.class) != null;
            boolean isMemberInfoClass = JWTMemberInfo.class.equals(parameter.getParameterType());

            return isMemberInfoAnnotation && isMemberInfoClass;
        }

        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            return httpServletRequest.getAttribute("memberInfo");
        }
}
