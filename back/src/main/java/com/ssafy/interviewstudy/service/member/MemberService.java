package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.member.dto.MemberProfileChangeDto;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final EntityManager em;

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
        return memberRepository.findMemberById(memberId);
    }

    @Transactional
    public void changeMemberNickname(Member member,String nickname){
        member.changeNickname(nickname);
        em.flush();
    }

    @Transactional
    public void changeMemberProfile(MemberProfileChangeDto memberProfileChangeDto){
        Member member = findMemberByMemberId(memberProfileChangeDto.getUserId());
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
