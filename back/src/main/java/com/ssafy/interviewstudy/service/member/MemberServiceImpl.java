package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.member.MemberStatus;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyRequest;
import com.ssafy.interviewstudy.dto.member.MemberProfileChangeDto;
import com.ssafy.interviewstudy.exception.member.MemberExceptionFactory;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.board.generalBoard.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.generalBoard.BoardRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.*;
import com.ssafy.interviewstudy.service.board.generalBoard.BoardService;
import com.ssafy.interviewstudy.service.study.studyMember.StudyMemberService;
import com.ssafy.interviewstudy.support.member.SocialLoginType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final ArticleCommentRepository articleCommentRepository;

    private final BoardRepository boardRepository;

    private final StudyRepository studyRepository;

    private final StudyRequestFileRepository studyRequestFileRepository;

    private final StudyRequestRepository studyRequestRepository;

    private final BoardService boardService;

    private final StudyMemberService studyMemberService;

    @Override
    public Member findByEmail(String email){
        return memberRepository.findUserByEmailAndStatusACTIVE(email).orElseThrow(MemberExceptionFactory::memberNotFound);
    }

    @Transactional
    @Override
    public void register(Member member){
        memberRepository.save(member);
    }

    @Override
    public Member checkDuplicateNickname(String nickname){
        Member member = memberRepository.findMemberByNicknameAndStatusACTIVE(nickname).orElseThrow(MemberExceptionFactory::memberNotFound);
        if(member==null || member.getStatus()!=MemberStatus.ACTIVE) return null;
        return member;
    }

    @Transactional
    @Override
    public void nextRegistrationStatus(Member member){
        member.nextRegistrationStatus();
    }

    //디버깅용
    @Override
    public Member findMemberByMemberId(Integer memberId){
        Member member = memberRepository.findMemberById(memberId).orElseThrow(MemberExceptionFactory::memberNotFound);
        if(member.getStatus()!=MemberStatus.ACTIVE) return null;
        return member;
    }

    @Transactional
    @Override
    public Boolean changeMemberNickname(Integer memberId, String nickname){
        Member curMember = memberRepository.findMemberById(memberId).orElseThrow(MemberExceptionFactory::memberNotFound);
        if(curMember==null) return false;
        curMember.changeNickname(nickname);
        return true;
    }

    @Transactional(readOnly = true)
    @Override
    public Member findByIdAndPlatform(String id, SocialLoginType socialLoginType){
        Member member = memberRepository.findMemberBySocialLoginIdAndSocialLoginTypeAndStatusACTIVE(id,socialLoginType).orElseThrow(MemberExceptionFactory::memberNotFound);
        if(member == null || member.getStatus()!= MemberStatus.ACTIVE) return null;
        return member;
    }

    @Transactional
    @Override
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
    @Override
    public boolean withdrawl(Integer memberId){
        Member member = memberRepository.findMemberById(memberId).orElseThrow();
        List<Study> list = studyRepository.findStudyByLeader(member);
        if(!list.isEmpty()){
            return false;
        }
        member.withdrawl();
        articleCommentRepository.deleteArticleCommentByAuthor(member);

        List<Board> articles = boardRepository.findAllByMember(member);
        for (Board article : articles) {
            boardService.removeArticle(article.getId());
        }

        List<Integer> studyIdList = studyRepository.findStudyIdByMember(member);
        for (Integer id : studyIdList) {
            studyMemberService.leaveStudy(id, memberId);
        }

        List<StudyRequest> requests = studyRequestRepository.findStudyRequestsByApplicant(member);
        for (StudyRequest request : requests) {
            studyRequestFileRepository.deleteByRequestId(request.getId());
            studyRequestRepository.deleteStudyRequestById(request.getId());
        }
        return true;
    }
}
