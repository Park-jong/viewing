package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
public class MemberService {

    private MemberRepository memberRepository;

    private EntityManager em;

    public MemberService(MemberRepository memberRepository, EntityManager em) {
        this.memberRepository = memberRepository;
        this.em = em;
    }

    public Member findByEmail(String email){
        Member member = memberRepository.findUserByEmail(email);
        return member;
    }

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
    @Transactional
    public Member findMemberByMemberId(Integer memberId){
        return memberRepository.findMemberByMemberId(memberId);
    }

    @Transactional
    public void changeMemberNickname(Member member,String nickname){
        member.changeNickname(nickname);
        em.flush();
    }
}
