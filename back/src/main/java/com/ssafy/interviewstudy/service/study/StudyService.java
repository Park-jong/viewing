package com.ssafy.interviewstudy.service.study;

import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyMember;
import com.ssafy.interviewstudy.dto.study.StudyResponse;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.StudyMemberRepository;
import com.ssafy.interviewstudy.repository.study.StudyRepository;
import com.ssafy.interviewstudy.repository.study.StudyRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StudyService {

    private StudyRepository studyRepository;
    private MemberRepository memberRepository;
    private StudyMemberRepository studyMemberRepository;

    @Autowired
    public StudyService(StudyRepository studyRepository, MemberRepository memberRepository, StudyMemberRepository studyMemberRepository) {
        this.studyRepository = studyRepository;
        this.memberRepository = memberRepository;
        this.studyMemberRepository = studyMemberRepository;
    }

    //내 스터디 조회
    public List<Study> findMyStudies(Integer id){
        return studyRepository.findStudiesByMember(memberRepository.findById(id).get());
    }

    //내가 찜한 스터디 조회
    public List<Study> findBookmarkStudies(Integer id){
        return studyRepository.findBookmarksByMember(memberRepository.findById(id).get());
    }

    //스터디 정보 조회
    public StudyResponse findStudyById(Integer id){
        Study study = studyRepository.findStudyById(id);
        return studyToResponse(study);
    }


    //스터디 조회
    public Page<StudyResponse> findStudies(Boolean option, Pageable pageable){
        Page<Study> studies = studyRepository.findStudiesBySearch(option, null, null, null, pageable);
        List<StudyResponse> result = new ArrayList<>();
        for (Study study : studies) {
            result.add(studyToResponse(study));
        }
        return new PageImpl<>(result, pageable, studies.getTotalElements());
    }

    //스터디 검색 결과 조회
    public Page<StudyResponse> findStudiesBySearch(Boolean option, Integer appliedCompanyId, String appliedJob, CareerLevel careerLevel, Pageable pageable){
        Page<Study> studies = studyRepository.findStudiesBySearch(option, appliedCompanyId, appliedJob, careerLevel, pageable);
        List<StudyResponse> result = new ArrayList<>();
        for (Study study : studies) {
            result.add(studyToResponse(study));
        }
        return new PageImpl<>(result, pageable, studies.getTotalElements());
    }

    //스터디 생성

    //스터디 삭제

    //스터디 정보 수정

    //스터디 가입 신청

    //스터디 가입 신청 조회

    //가입 신청 승인

    //가입 신청 거절

    //스터디 탈퇴

    //스터디 찜하기

    //스터디원 추방

    //스터디장 위임

    //스터디원 목록 확인

    //스터디 실시간 채팅 작성

    //스터디 실시간 채팅 조회

    //스터디 일정 조회

    //스터디 일정 추가

    //스터디 일정 수정

    //스터디 일정 삭제


    private StudyResponse studyToResponse(Study study){
        StudyResponse studyResponse = new StudyResponse(study);
        studyResponse.headCounting(studyMemberRepository.countStudyMemberByStudy(study));
        return studyResponse;
    }
}
