package com.ssafy.interviewstudy.repository.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.StudyMember;
import com.ssafy.interviewstudy.support.member.SocialLoginType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Integer> {

    Member findMemberBySocialLoginIdAndSocialLoginType(String socialLoginId, SocialLoginType socialLoginType);

    Member findUserByEmail(String email);

    Member findMemberByNickname(String nickname);

    //디버깅용
    Member findMemberById(Integer memberId);

}