package com.ssafy.interviewstudy.service.member.bookmark;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyBookmark;
import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkRequest;
import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkResponse;
import com.ssafy.interviewstudy.exception.message.CreationFailException;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.member.MemberStudyBookmarkRepository;
import com.ssafy.interviewstudy.repository.study.StudyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
@Service
public class MemberStudyBookmarkService {
    private final MemberRepository memberRepository;
    private final MemberStudyBookmarkRepository memberStudyBookmarkRepository;
    
    private final StudyRepository studyRepository;

    @Autowired
    public MemberStudyBookmarkService(MemberRepository memberRepository, MemberStudyBookmarkRepository memberStudyBookmarkRepository, StudyRepository studyRepository) {
        this.memberRepository = memberRepository;
        this.memberStudyBookmarkRepository = memberStudyBookmarkRepository;
        this.studyRepository = studyRepository;
    }

    @Transactional
    public StudyBookmarkResponse createStudyBookmark(@Valid StudyBookmarkRequest studyBookmarkRequest){

        //먼저 이미 북마크했는지 검증
        StudyBookmark validStudyBookmark =
                memberStudyBookmarkRepository.findStudyBookmarkByStudyIdAndMemberId(
                                studyBookmarkRequest.getStudyId(),
                                studyBookmarkRequest.getMemberId());

        if(validStudyBookmark!=null){
            throw new CreationFailException("스터디 북마크");
        }

        //스터디 북마크할 멤버 조회
        Member member = memberRepository.findMemberById(studyBookmarkRequest.getMemberId());


        //북마크 대상 스터디 조회
        Study study = studyRepository.findStudyById(studyBookmarkRequest.getStudyId());

        System.out.println("스터디 : "+study);

        //이 경우 bad request 로 처리하는게 좋아보임
        // memberId가 잘못되었는지 studyId가 잘못되었는지 알려줘야함
        // 향후 리팩토링
        if(member==null || study==null){
            throw new CreationFailException("스터디 북마크");
        }

        StudyBookmark studyBookmark = StudyBookmark.builder().study(study).member(member).build();

        memberStudyBookmarkRepository.save(studyBookmark);

        return new StudyBookmarkResponse(studyBookmark.getId());
    }


    @Transactional
    public void deleteStudyBookmark(@Valid StudyBookmarkRequest studyBookmarkRequest){

        //멤버아이디와 스터디아이디로 해당 북마크 지우기
        Integer result =
                memberStudyBookmarkRepository
                        .deleteStudyBookmarkByStudyIdAndMemberId(
                                studyBookmarkRequest.getStudyId(),
                                studyBookmarkRequest.getMemberId());

        if(result==null){
            throw new NotFoundException("스터디 북마크");
        }
    }

    @Transactional
    public Boolean checkStudyBookmarkByMemberId(Integer memberId,Integer studyId){
        return memberStudyBookmarkRepository.findStudyBookmarkByStudyIdAndMemberId(studyId,memberId) != null;
    }
}
