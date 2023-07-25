package com.ssafy.interviewstudy.service.study;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.*;
import com.ssafy.interviewstudy.dto.study.*;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final CompanyRepository companyRepository;
    private final StudyTagRepository studyTagRepository;
    private final StudyTagTypeRepository studyTagTypeRepository;
    private final StudyRequestRepository studyRequestRepository;
    private final StudyRequestFileRepository studyRequestFileRepository;


    //내 스터디 조회
    public List<Study> findMyStudies(Integer id){
        return studyRepository.findStudiesByMember(memberRepository.findById(id).get());
    }

    //내가 찜한 스터디 조회
    public List<Study> findBookmarkStudies(Integer id){
        return studyRepository.findBookmarksByMember(memberRepository.findById(id).get());
    }

    //스터디 정보 조회
    public StudyDtoResponse findStudyById(Integer id){
        Study study = studyRepository.findStudyById(id);
        return studyToResponse(study);
    }


    //스터디 조회
    public Page<StudyDtoResponse> findStudies(Boolean option, Pageable pageable){
        Page<Study> studies = studyRepository.findStudiesBySearch(option, null, null, null, pageable);
        List<StudyDtoResponse> result = new ArrayList<>();
        for (Study study : studies) {
            result.add(studyToResponse(study));
        }
        return new PageImpl<>(result, pageable, studies.getTotalElements());
    }

    //스터디 검색 결과 조회
    public Page<StudyDtoResponse> findStudiesBySearch(Boolean option, Integer appliedCompanyId, String appliedJob, CareerLevel careerLevel, Pageable pageable){
        Page<Study> studies = studyRepository.findStudiesBySearch(option, appliedCompanyId, appliedJob, careerLevel, pageable);
        List<StudyDtoResponse> result = new ArrayList<>();
        for (Study study : studies) {
            result.add(studyToResponse(study));
        }
        return new PageImpl<>(result, pageable, studies.getTotalElements());
    }

    @Transactional
    //스터디 생성
    public Integer addStudy(StudyDtoRequest studyDtoRequest){
        Study study = requestToStudy(studyDtoRequest);
        Member leader = memberRepository.findById(studyDtoRequest.getLeaderId()).get();
        study.updateLeader(leader);
        study.updateCompany(companyRepository.findCompanyByName(studyDtoRequest.getAppliedCompany()).get());
        studyRepository.save(study);

        List<Integer> tags = studyDtoRequest.getTags();
        for (Integer tag : tags) {
            StudyTagType stt = studyTagTypeRepository.findById(tag).get();
            StudyTag st = new StudyTag(study, stt);
            studyTagRepository.save(st);
        }

        StudyMember studyMember = new StudyMember(study, leader);
        studyMemberRepository.save(studyMember);

        return study.getId();
    }

    //스터디 삭제
    @Transactional
    public void removeStudy(Integer studyId){
        Study study = studyRepository.findById(studyId).get();
        study.deleteStudy();
        studyMemberRepository.deleteStudyMemberByStudy(study);
    }

    //스터디 정보 수정
    @Transactional
    public void modifyStudy(Integer studyId, StudyDtoRequest studyDtoRequest){
        Study study = studyRepository.findById(studyId).get();
        study.updateStudy(studyDtoRequest);

        studyTagRepository.deleteStudyTagByStudy(study);

        List<Integer> tags = studyDtoRequest.getTags();
        for (Integer tag : tags) {
            StudyTagType stt = studyTagTypeRepository.findById(tag).get();
            StudyTag st = new StudyTag(study, stt);
            studyTagRepository.save(st);
        }
    }

    //스터디 가입 신청
    @Transactional
    public Integer addRequest(Integer studyId, RequestDto requestDto){
        Study study = studyRepository.findById(studyId).get();
        Member member = memberRepository.findById(requestDto.getMemberId()).get();
        StudyRequest studyRequest = new StudyRequest(study, member, requestDto);

        studyRequestRepository.save(studyRequest);

        List<RequestFile> files = requestDto.getRequestFiles();
        for (RequestFile file : files) {
            StudyRequestFile studyRequestFile = new StudyRequestFile(file, studyRequest);
            studyRequestFileRepository.save(studyRequestFile);
        }

        return studyRequest.getId();
    }

    //스터디 가입 신청 조회
    public List<RequestDtoResponse> findRequestsByStudy(Integer studyId){
        Study study = studyRepository.findById(studyId).get();
        List<StudyRequest> requests = studyRequestRepository.findStudyRequestsByStudy(study);

        List<RequestDtoResponse> result = new ArrayList<>();

        for (StudyRequest request : requests) {
            Author user = new Author(request.getApplicant());
            RequestDtoResponse response = new RequestDtoResponse(request.getId(), user, request.getIntroduction(), request.getRequestedAt(), null);
            result.add(response);
        }
        return result;
    }

    //스터디 가입 신청 개별 조회
    public RequestDtoResponse findRequestById(Integer id){
        StudyRequest request = studyRequestRepository.findStudyRequestById(id).get();
        List<StudyRequestFile> files = request.getStudyRequestFiles();
        List<RequestFile> reponseFiles = new ArrayList<>();
        for (StudyRequestFile file : files) {
            reponseFiles.add(new RequestFile(file));
        }
        return new RequestDtoResponse(request.getId(), new Author(request.getApplicant()), request.getIntroduction(), request.getRequestedAt(), reponseFiles);
    }

    //가입 신청 승인
    @Transactional
    public void permitRequest(Integer requestId){
        StudyRequest studyRequest = studyRequestRepository.findStudyAndMemberById(requestId).get();
        deleteRequest(requestId);
        StudyMember sm = new StudyMember(studyRequest.getStudy(), studyRequest.getApplicant());
        studyMemberRepository.save(sm);
    }

    //가입 신청 거절
    @Transactional
    public void rejectRequest(Integer requestId){
        deleteRequest(requestId);
    }

    //스터디 탈퇴
    @Transactional
    public void leaveStudy(Integer studyId, Integer memberId){
        studyMemberRepository.deleteStudyMemberByStudyIdAndMemberId(studyId, memberId);
    }

    //스터디원 추방
    @Transactional
    public void banMemberStudy(Integer studyId, Integer memberId){
        studyMemberRepository.deleteStudyMemberByStudyIdAndMemberId(studyId, memberId);
    }

    //스터디장 위임
    @Transactional
    public void delegateLeader(Integer studyId, Integer memberId){
        Study study = studyRepository.findById(studyId).get();
        Member member = memberRepository.findById(memberId).get();
        study.updateLeader(member);
    }

    //스터디원 목록 확인
    public List<Author> findStudyMembers(Integer studyId){
        List<Member> members = studyMemberRepository.findMembersByStudyId(studyId);
        List<Author> result = new ArrayList<>();
        for (Member member : members) {
            result.add(new Author(member));
        }
        return result;
    }

    //스터디 실시간 채팅 작성
    public void addChat(Integer studyId, ChatRequest chat){
        Member member = memberRepository.findById(chat.getUserId()).get();
        Study study = studyRepository.findById(studyId).get();
        StudyChat studyChat = new StudyChat(study, member, chat.getContent());
    }

    //스터디 실시간 채팅 조회

    //스터디 일정 조회

    //스터디 일정 추가

    //스터디 일정 수정

    //스터디 일정 삭제


    private StudyDtoResponse studyToResponse(Study study){
        StudyDtoResponse studyDtoResponse = new StudyDtoResponse(study);
        studyDtoResponse.headCounting(studyMemberRepository.countStudyMemberByStudy(study));
        return studyDtoResponse;
    }

    private Study requestToStudy(StudyDtoRequest studyDtoRequest){
        Study study = new Study(studyDtoRequest);
        return study;
    }

    private void deleteRequest(Integer requestId){
        studyRequestFileRepository.deleteByRequestId(requestId);
        studyRequestRepository.deleteStudyRequestById(requestId);
    }
}
