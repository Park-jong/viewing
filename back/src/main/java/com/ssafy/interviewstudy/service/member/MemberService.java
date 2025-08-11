package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.member.MemberProfileChangeDto;
import com.ssafy.interviewstudy.support.member.SocialLoginType;
import org.springframework.transaction.annotation.Transactional;

public interface MemberService {
    Member findByEmail(String email);

    void register(Member member);

    Member checkDuplicateNickname(String nickname);

    void nextRegistrationStatus(Member member);

    //디버깅용
    Member findMemberByMemberId(Integer memberId);

    Boolean changeMemberNickname(Integer memberId, String nickname);

    Member findByIdAndPlatform(String id, SocialLoginType socialLoginType);

    void changeMemberProfile(MemberProfileChangeDto memberProfileChangeDto);

    boolean withdrawl(Integer memberId);
}
