package com.ssafy.interviewstudy.service.study;

import com.querydsl.core.Tuple;
import com.ssafy.interviewstudy.domain.calendar.Calendar;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.domain.study.*;
import com.ssafy.interviewstudy.dto.calendar.CalendarListResponse;
import com.ssafy.interviewstudy.dto.calendar.CalendarRetrieveResponse;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.dto.notification.NotificationStudyDto;
import com.ssafy.interviewstudy.dto.study.*;
import com.ssafy.interviewstudy.exception.calendar.updateFailException;
import com.ssafy.interviewstudy.exception.message.CreationFailException;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.calendar.CalendarRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.*;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import com.ssafy.interviewstudy.support.file.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyServiceImpl implements StudyService {

    private final StudyRepository studyRepository;
    private final MemberRepository memberRepository;
    private final StudyMemberRepository studyMemberRepository;
    private final CompanyRepository companyRepository;
    private final StudyTagRepository studyTagRepository;
    private final StudyTagTypeRepository studyTagTypeRepository;
    private final StudyRequestRepository studyRequestRepository;
    private final StudyBookmarkRepository studyBookmarkRepository;

    //내 스터디 조회
    @Override
    public List<StudyDtoResponse> findMyStudies(Integer id) {
        Member member = memberRepository.findById(id).get();
        //태그들 가져옴
        List<Study> studies = studyRepository.findStudiesByMember(memberRepository.findById(id).get());
        //멤버수 가져옴(스터디, 멤버수)
        List<Tuple> counts = studyRepository.findMyStudyMemberCountByMember(member);
        List<StudyDtoResponse> result = new ArrayList<>();
        for (Tuple tuple : counts) {
            result.add(new StudyDtoResponse(tuple.get(0, Study.class), tuple.get(1, Boolean.class), tuple.get(2, Long.class)));
        }
        return result;
    }

    //내가 찜한 스터디 조회
    @Override
    public List<StudyDtoResponse> findBookmarkStudies(Integer id) {
        Member member = memberRepository.findById(id).get();
        //1차 캐시에 올림(태그들)
        List<Study> studies = studyRepository.findBookmarksByMember(member);
        //(Study, 멤버수)
        List<Tuple> counts = studyRepository.findBookmarksMemberCountByMember(member);
        List<StudyDtoResponse> result = new ArrayList<>();
        for (Tuple tuple : counts) {
            result.add(new StudyDtoResponse(tuple.get(0, Study.class), true, tuple.get(1, Long.class)));
        }
        return result;
    }

    //스터디 정보 조회
    @Override
    public StudyDtoResponse findStudyById(JWTMemberInfo memberInfo, Integer id) {
        Integer memberId = memberInfo.getMemberId();
        Study study = studyRepository.findStudyById(id);
        checkExist(study);
        long headCount = studyMemberRepository.countStudyMemberByStudy(study);
        StudyBookmark sb = studyBookmarkRepository.findStudyBookmarkByStudyIdAndMemberId(id, memberId);
        return new StudyDtoResponse(study, sb != null, headCount);
    }

    private void checkExist(Study study) {
        if (study == null || study.getIsDelete()) throw new NotFoundException("스터디를 찾을 수 없습니다.");
    }

    //참가한 스터디 자세히보기
    @Override
    public StudyDetailDtoResponse findStudyDetailById(JWTMemberInfo memberInfo, Integer id) {
        Integer memberId = memberInfo.getMemberId();
        Study study = studyRepository.findStudyById(id);
        checkExist(study);
        studyMemberRepository.findMembersByStudy(study);
        return new StudyDetailDtoResponse(study);
    }

    //스터디 검색 결과 조회
    @Override
    public Page<StudyDtoResponse> findStudiesBySearch(JWTMemberInfo memberInfo, Boolean option, String appliedCompany, String appliedJob, CareerLevel careerLevel, Integer tag, Pageable pageable) {
        Integer memberId = memberInfo != null ? memberInfo.getMemberId() : null;
        //검색 결과 (study_id, Study, 북마크여부, 인원)
        Page<Tuple> studies = studyRepository.findStudiesBySearch(option, appliedCompany, appliedJob, careerLevel, tag, pageable);
        List<StudyDtoResponse> result = new ArrayList<>();
        List<Integer> studyids = new ArrayList<>();
        for (Tuple study : studies) {
            studyids.add(study.get(0, Integer.class));
        }
        //태그들을 가져옴
        List<Study> byIds = studyRepository.findByIds(studyids);
        List<Tuple> etc = studyRepository.isBookmark(memberId, studyids);
        List<Tuple> content = studies.getContent();
        for (int i = 0, size = content.size(); i < size; i++) {
            result.add(new StudyDtoResponse(content.get(i).get(1, Study.class), etc.get(i).get(0, Boolean.class), etc.get(i).get(1, Long.class)));
        }
        return new PageImpl<>(result, pageable, studies.getTotalElements());
    }

    @Transactional
    @Override
    public Integer addStudy(StudyDtoRequest studyDtoRequest) {
        Study study = requestToStudy(studyDtoRequest);
        Member leader = memberRepository.findById(studyDtoRequest.getLeaderId()).get();
        study.updateLeader(leader);
        Optional<Company> companyByName = companyRepository.findCompanyByName(studyDtoRequest.getAppliedCompany());
        if (companyByName.isEmpty()) throw new NotFoundException("해당 정보를 찾을 수 없음");
        Company company = companyByName.get();
        study.updateCompany(company);
        studyRepository.save(study);
        //태그들 추가
        List<Integer> tags = studyDtoRequest.getTags();
        for (Integer tag : tags) {
            StudyTagType stt = studyTagTypeRepository.findById(tag).get();
            StudyTag st = new StudyTag(study, stt);
            studyTagRepository.save(st);
        }

        StudyMember studyMember = new StudyMember(study, leader);
        studyMember.updateLeader(true);
        studyMemberRepository.save(studyMember);

        return study.getId();
    }

    private Study requestToStudy(StudyDtoRequest studyDtoRequest) {
        Study study = new Study(studyDtoRequest);
        return study;
    }

    //스터디 삭제
    @Transactional
    @Override
    public void removeStudy(Integer studyId) {
        Study study = studyRepository.findById(studyId).get();
        study.updateLeader(null);
        study.deleteStudy();
        studyMemberRepository.deleteStudyMemberByStudy(study);
    }

    //스터디 정보 수정
    @Transactional
    @Override
    public void modifyStudy(Integer studyId, StudyDtoRequest studyDtoRequest) {
        Optional<Study> studyOp = studyRepository.findById(studyId);

        if (studyOp == null) throw new NotFoundException("스터디를 찾을 수 없습니다.");

        Study study = studyOp.get();

        study.updateStudy(studyDtoRequest);

        studyTagRepository.deleteStudyTagByStudy(study);

        List<Integer> tags = studyDtoRequest.getTags();
        for (Integer tag : tags) {
            StudyTagType stt = studyTagTypeRepository.findById(tag).get();
            StudyTag st = new StudyTag(study, stt);
            studyTagRepository.save(st);
        }
    }



    //유효한 요청인지 체크
    private StudyRequest checkRequest(Integer requestId, Integer studyId, Integer memberId) {
        Optional<Study> studyOp = studyRepository.findById(studyId);
        Optional<Member> memberOp = memberRepository.findById(memberId);
        if (studyOp.isEmpty() || studyOp.get().getIsDelete() || memberOp.isEmpty()) {
            return null;
        }

        Optional<StudyRequest> studyRequestOp = studyRequestRepository.findStudyAndMemberById(requestId);
        if (studyRequestOp.isEmpty() || studyRequestOp.get().getApplicant().getId() != memberId || studyRequestOp.get().getStudy().getId() != studyId) {
            return null;
        }
        return studyRequestOp.get();
    }

    //스터디 유효성 체크
    @Override
    public boolean checkStudy(Integer studyId) {
        Optional<Study> study = studyRepository.findById(studyId);
        return study.isPresent() && !study.get().getIsDelete();
    }
}
