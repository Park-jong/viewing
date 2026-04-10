package com.ssafy.interviewstudy.service.study;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.*;
import com.ssafy.interviewstudy.dto.study.StudyDtoRequest;
import com.ssafy.interviewstudy.exception.member.MemberExceptionFactory;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.exception.study.StudyExceptionFactory;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StudyCreationServiceImpl implements StudyCreationService {

    private final MemberRepository memberRepository;
    private final CompanyRepository companyRepository;
    private final StudyRepository studyRepository;
    private final StudyTagRepository studyTagRepository;
    private final StudyTagTypeRepository studyTagTypeRepository;
    private final StudyMemberRepository studyMemberRepository;

    @Override
    public Integer addStudy(StudyDtoRequest studyDtoRequest) {
        Study study = new Study(studyDtoRequest);

        Member leader = memberRepository.findById(studyDtoRequest.getLeaderId())
                .orElseThrow(MemberExceptionFactory::memberNotFound);
        study.updateLeader(leader);

        Company company = companyRepository.findCompanyByName(studyDtoRequest.getAppliedCompany())
                .orElseThrow(() -> new NotFoundException("해당 정보를 찾을 수 없음"));
        study.updateCompany(company);

        studyRepository.save(study);

        saveTags(study, studyDtoRequest.getTags());

        StudyMember studyMember = new StudyMember(study, leader);
        studyMember.updateLeader(true);
        studyMemberRepository.save(studyMember);

        return study.getId();
    }

    private void saveTags(Study study, List<Integer> tags) {
        for (Integer tag : tags) {
            StudyTagType stt = studyTagTypeRepository.findById(tag).orElseThrow(StudyExceptionFactory::studyNotFound);
            studyTagRepository.save(new StudyTag(study, stt));
        }
    }
}
