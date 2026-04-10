package com.ssafy.interviewstudy.service.study;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.*;
import com.ssafy.interviewstudy.dto.study.StudyDtoRequest;
import com.ssafy.interviewstudy.exception.member.NotFoundException;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class StudyCreationServiceTest {

    @Mock private MemberRepository memberRepository;
    @Mock private CompanyRepository companyRepository;
    @Mock private StudyRepository studyRepository;
    @Mock private StudyTagRepository studyTagRepository;
    @Mock private StudyTagTypeRepository studyTagTypeRepository;
    @Mock private StudyMemberRepository studyMemberRepository;
    @InjectMocks private StudyCreationServiceImpl studyCreationService;

    Member mockLeader;
    Company mockCompany;
    StudyTagType mockTagType;
    StudyDtoRequest mockRequest;

    final int leaderId = 1;
    final int studyId = 10;
    final int tagId = 1;

    @BeforeEach
    void setUp() {
        mockLeader = Member.builder().id(leaderId).nickname("스터디장").build();
        mockCompany = Mockito.mock(Company.class);
        mockTagType = StudyTagType.builder().id(tagId).tagName("백엔드").build();
        mockRequest = StudyDtoRequest.builder()
                .title("신규 스터디")
                .description("스터디 설명")
                .appliedCompany("삼성")
                .appliedJob("백엔드")
                .capacity(6)
                .recruitment(true)
                .deadline(LocalDateTime.now().plusDays(14))
                .leaderId(leaderId)
                .careerLevel(CareerLevel.INTERN)
                .tags(List.of(tagId))
                .build();
    }

    // ── 스터디 생성 ────────────────────────────────────────

    @Test
    void addStudy() {
        Mockito.when(memberRepository.findById(leaderId)).thenReturn(Optional.of(mockLeader));
        Mockito.when(companyRepository.findCompanyByName("삼성")).thenReturn(Optional.of(mockCompany));
        Mockito.doAnswer(invocation -> {
            Study study = invocation.getArgument(0);
            ReflectionTestUtils.setField(study, "id", studyId);
            return study;
        }).when(studyRepository).save(any(Study.class));
        Mockito.when(studyTagTypeRepository.findById(tagId)).thenReturn(Optional.of(mockTagType));

        Integer result = studyCreationService.addStudy(mockRequest);

        assertThat(result).isEqualTo(studyId);
        verify(studyRepository, times(1)).save(any(Study.class));
        verify(studyTagRepository, times(1)).save(any(StudyTag.class));
        verify(studyMemberRepository, times(1)).save(any(StudyMember.class));
    }

    @Test
    void addStudy_multipleTags_savesAllTags() {
        StudyTagType tagType2 = StudyTagType.builder().id(2).tagName("프론트엔드").build();
        StudyDtoRequest multiTagRequest = StudyDtoRequest.builder()
                .title("신규 스터디")
                .appliedCompany("삼성")
                .appliedJob("풀스택")
                .capacity(6)
                .recruitment(true)
                .deadline(LocalDateTime.now().plusDays(14))
                .leaderId(leaderId)
                .tags(List.of(tagId, 2))
                .build();
        Mockito.when(memberRepository.findById(leaderId)).thenReturn(Optional.of(mockLeader));
        Mockito.when(companyRepository.findCompanyByName("삼성")).thenReturn(Optional.of(mockCompany));
        Mockito.when(studyRepository.save(any(Study.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(studyTagTypeRepository.findById(tagId)).thenReturn(Optional.of(mockTagType));
        Mockito.when(studyTagTypeRepository.findById(2)).thenReturn(Optional.of(tagType2));

        studyCreationService.addStudy(multiTagRequest);

        verify(studyTagRepository, times(2)).save(any(StudyTag.class));
    }

    @Test
    void addStudy_memberNotFound_throwsException() {
        Mockito.when(memberRepository.findById(leaderId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyCreationService.addStudy(mockRequest))
                .isInstanceOf(NotFoundException.class);

        verify(studyRepository, never()).save(any());
    }

    @Test
    void addStudy_companyNotFound_throwsException() {
        Mockito.when(memberRepository.findById(leaderId)).thenReturn(Optional.of(mockLeader));
        Mockito.when(companyRepository.findCompanyByName("삼성")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyCreationService.addStudy(mockRequest))
                .isInstanceOf(com.ssafy.interviewstudy.exception.message.NotFoundException.class);

        verify(studyRepository, never()).save(any());
    }

    @Test
    void addStudy_tagNotFound_throwsException() {
        Mockito.when(memberRepository.findById(leaderId)).thenReturn(Optional.of(mockLeader));
        Mockito.when(companyRepository.findCompanyByName("삼성")).thenReturn(Optional.of(mockCompany));
        Mockito.when(studyRepository.save(any(Study.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Mockito.when(studyTagTypeRepository.findById(tagId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyCreationService.addStudy(mockRequest))
                .isInstanceOf(com.ssafy.interviewstudy.exception.study.NotFoundException.class);

        verify(studyMemberRepository, never()).save(any());
    }
}
