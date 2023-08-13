package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.member.MemberStatus;
import com.ssafy.interviewstudy.dto.member.MemberProfileChangeDto;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.board.StudyBoardRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.member.MemberStudyRepository;
import com.ssafy.interviewstudy.support.member.SocialLoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    private final StudyBoardRepository studyBoardRepository;

    private final MemberStudyRepository memberStudyRepository;

    public Member findByEmail(String email){
        Member member = memberRepository.findUserByEmailAndStatusACTIVE(email);
        return member;
    }

    @Transactional
    public void register(Member member){
        memberRepository.save(member);
    }

    public Member checkDuplicateNickname(String nickname){
        Member member = memberRepository.findMemberByNicknameAndStatusACTIVE(nickname);
        if(member==null || member.getStatus()!=MemberStatus.ACTIVE) return null;
        return member;
    }

    @Transactional
    public void nextRegistrationStatus(Member member){
        member.nextRegistrationStatus();
    }

    //디버깅용
    public Member findMemberByMemberId(Integer memberId){
        Member member = memberRepository.findMemberById(memberId);
        if(member.getStatus()!=MemberStatus.ACTIVE) return null;
        return member;
    }

    @Transactional
    public Boolean changeMemberNickname(Integer memberId,String nickname){
        Member curMember = memberRepository.findMemberById(memberId);
        if(curMember==null) return false;
        curMember.changeNickname(nickname);
        return true;
    }

    @Transactional(readOnly = true)
    public Member findByIdAndPlatform(String id, SocialLoginType socialLoginType){
        Member member = memberRepository.findMemberBySocialLoginIdAndSocialLoginTypeAndStatusACTIVE(id,socialLoginType);
        if(member == null || member.getStatus()!= MemberStatus.ACTIVE) return null;
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

    @Transactional
    public void withdrawl(Integer memberId){
        if(memberId == null){
            throw new NotFoundException("멤버 정보가 없습니다");
        }
        Member member = memberRepository.findMemberById(memberId);
        if(member == null){
            throw new NotFoundException("멤버 정보가 없습니다.");
        }
        member.withdrawl();
    }
}
