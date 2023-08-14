package com.ssafy.interviewstudy.repository.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.support.member.SocialLoginType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member,Integer> {

    @Query("select m from Member m Where m.socialLoginId = :socialLoginId and m.socialLoginType = :socialLoginType and m.status='ACTIVE'")
    Member findMemberBySocialLoginIdAndSocialLoginTypeAndStatusACTIVE(String socialLoginId, SocialLoginType socialLoginType);

    @Query("select m from Member m Where m.email = :email and m.status='ACTIVE'")
    Member findUserByEmailAndStatusACTIVE(String email);

    @Query("select m from Member m Where m.nickname = :nickname and m.status='ACTIVE'")
    Member findMemberByNicknameAndStatusACTIVE(String nickname);

    //디버깅용
    @Query("select m from Member m where m.id = :memberId and m.status='ACTIVE'")
    Member findMemberById(Integer memberId);

}