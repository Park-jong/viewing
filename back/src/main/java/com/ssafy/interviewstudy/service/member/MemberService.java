package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.member.MemberProfileChangeDto;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.support.member.SocialLoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final EntityManager em;

    public Member findByEmail(String email){
        Member member = memberRepository.findUserByEmail(email);
        return member;
    }

    @Transactional
    public void register(Member member){
        memberRepository.save(member);
    }

    public Member checkDuplicateNickname(String nickname){
        return memberRepository.findMemberByNickname(nickname);
    }

    @Transactional
    public void nextRegistrationStatus(Member member){
        member.nextRegistrationStatus();
        em.flush();
    }

    //디버깅용
    public Member findMemberByMemberId(Integer memberId){
        return memberRepository.findMemberById(memberId);
    }

    @Transactional
    public void changeMemberNickname(Member member,String nickname){
        member.changeNickname(nickname);
        em.flush();
    }

    @Transactional(readOnly = true)
    public Member findByIdAndPlatform(String id, SocialLoginType socialLoginType){
        Member member = memberRepository.findMemberBySocialLoginIdAndSocialLoginType(id,socialLoginType);
        return member;
    }

    @Transactional
    public void changeMemberProfile(MemberProfileChangeDto memberProfileChangeDto){
        Member member = findMemberByMemberId(memberProfileChangeDto.getMemberId());
        if(member==null) throw new NotFoundException("유저가 존재하지 않습니다");
        if(memberProfileChangeDto.getCharacter()!=null){
            member.changeMemberProfileImage(
                    memberProfileChangeDto.getCharacter()
            );
        }
        if(memberProfileChangeDto.getBackground()!=null){
            member.changeMemberProfileBackground(
                    memberProfileChangeDto.getBackground()
            );
        }
    }
}
