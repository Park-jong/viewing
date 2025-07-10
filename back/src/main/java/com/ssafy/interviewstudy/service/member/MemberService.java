package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.member.MemberProfileChangeDto;
import com.ssafy.interviewstudy.support.member.SocialLoginType;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService {
    Member findByEmail(String email);

    @Transactional
    void register(Member member);

    Member checkDuplicateNickname(String nickname);

    @Transactional
    void nextRegistrationStatus(Member member);

    //디버깅용
    Member findMemberByMemberId(Integer memberId);

    @Transactional
    Boolean changeMemberNickname(Integer memberId, String nickname);

    @Transactional(readOnly = true)
    Member findByIdAndPlatform(String id, SocialLoginType socialLoginType);

    @Transactional
    void changeMemberProfile(MemberProfileChangeDto memberProfileChangeDto);

    @Transactional
    boolean withdrawl(Integer memberId);
}
