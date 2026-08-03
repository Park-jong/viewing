package com.ssafy.interviewstudy.service.member.bookmark;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyBookmark;
import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkRequest;
import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkResponse;
import com.ssafy.interviewstudy.exception.member.MemberExceptionFactory;
import com.ssafy.interviewstudy.exception.message.CreationFailException;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.member.MemberStudyBookmarkRepository;
import com.ssafy.interviewstudy.repository.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class MemberStudyBookmarkServiceImpl implements MemberStudyBookmarkService {
    private final MemberRepository memberRepository;
    private final MemberStudyBookmarkRepository memberStudyBookmarkRepository;
    private final StudyRepository studyRepository;

    @Transactional
    @Override
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
        Member member = memberRepository.findMemberById(studyBookmarkRequest.getMemberId()).orElseThrow(MemberExceptionFactory::memberNotFound);


        //북마크 대상 스터디 조회
        Study study = studyRepository.findStudyById(studyBookmarkRequest.getStudyId());
        if(study==null){
            throw new CreationFailException("스터디 북마크");
        }

        StudyBookmark studyBookmark = StudyBookmark.builder().study(study).member(member).build();

        memberStudyBookmarkRepository.save(studyBookmark);

        return new StudyBookmarkResponse(studyBookmark.getId());
    }


    @Transactional
    @Override
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

    @Override
    public Boolean checkStudyBookmarkByMemberId(Integer memberId, Integer studyId){
        return memberStudyBookmarkRepository.findStudyBookmarkByStudyIdAndMemberId(studyId, memberId) != null;
    }
}
