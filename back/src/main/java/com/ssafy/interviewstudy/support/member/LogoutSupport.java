package com.ssafy.interviewstudy.support.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class LogoutSupport {

    private final MemberRepository memberRepository;
    public Boolean Logout(JWTMemberInfo jwtMemberInfo){
        Member member = memberRepository.findMemberById(jwtMemberInfo.getMemberId());
        return false;
    }
}
