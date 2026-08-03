package com.ssafy.interviewstudy.service.study;

import com.querydsl.core.Tuple;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.*;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.dto.study.StudyDetailDtoResponse;
import com.ssafy.interviewstudy.dto.study.StudyDtoRequest;
import com.ssafy.interviewstudy.dto.study.StudyDtoResponse;
import com.ssafy.interviewstudy.repository.study.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class StudyServiceTest {

    @Mock private StudyRepository studyRepository;
    @Mock private StudyMemberRepository studyMemberRepository;
    @Mock private StudyTagRepository studyTagRepository;
    @Mock private StudyTagTypeRepository studyTagTypeRepository;
    @Mock private StudyBookmarkRepository studyBookmarkRepository;
    @InjectMocks private StudyServiceImpl studyService;

    Member mockMember;
    Study mockStudy;
    Company mockCompany;
    JWTMemberInfo mockMemberInfo;
    StudyDtoRequest mockRequest;
    StudyMember mockStudyMember;

    final int memberId = 1;
    final int studyId = 1;

    @BeforeEach
    void setUp() {
        mockMember = Member.builder().id(memberId).nickname("홍길동").build();
        mockCompany = Mockito.mock(Company.class);
        mockStudyMember = StudyMember.builder().build();
        mockStudy = Study.builder().id(studyId).studyTags(List.of()).studyMembers(List.of(mockStudyMember)).build();
        mockStudy.updateLeader(mockMember);
        mockStudy.updateCompany(mockCompany);
        mockMemberInfo = JWTMemberInfo.builder().memberId(memberId).email("test@test.com").build();
        mockRequest = StudyDtoRequest.builder()
                .title("수정된 스터디")
                .description("수정 설명")
                .appliedCompany("삼성")
                .appliedJob("백엔드")
                .capacity(5)
                .recruitment(true)
                .deadline(LocalDateTime.now().plusDays(7))
                .tags(List.of())
                .build();
    }

    // ── 내 스터디 조회 ─────────────────────────────────────

    @Test
    void findMyStudies() {
        Tuple mockTuple = Mockito.mock(Tuple.class);
        Mockito.when(mockTuple.get(0, Study.class)).thenReturn(mockStudy);
        Mockito.when(mockTuple.get(1, Boolean.class)).thenReturn(false);
        Mockito.when(mockTuple.get(2, Long.class)).thenReturn(1L);
        Mockito.when(studyRepository.findStudiesByMemberId(memberId)).thenReturn(List.of());
        Mockito.when(studyRepository.findMyStudyMemberCountByMemberId(memberId)).thenReturn(List.of(mockTuple));

        List<StudyDtoResponse> result = studyService.findMyStudies(memberId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStudyId()).isEqualTo(studyId);
        assertThat(result.get(0).getHeadCount()).isEqualTo(1L);
    }

    @Test
    void findMyStudies_empty() {
        Mockito.when(studyRepository.findStudiesByMemberId(memberId)).thenReturn(List.of());
        Mockito.when(studyRepository.findMyStudyMemberCountByMemberId(memberId)).thenReturn(List.of());

        List<StudyDtoResponse> result = studyService.findMyStudies(memberId);

        assertThat(result).isEmpty();
    }

    // ── 북마크 스터디 조회 ─────────────────────────────────

    @Test
    void findBookmarkStudies() {
        Tuple mockTuple = Mockito.mock(Tuple.class);
        Mockito.when(mockTuple.get(0, Study.class)).thenReturn(mockStudy);
        Mockito.when(mockTuple.get(1, Long.class)).thenReturn(2L);
        Mockito.when(studyRepository.findBookmarksByMemberId(memberId)).thenReturn(List.of());
        Mockito.when(studyRepository.findBookmarksMemberCountByMemberId(memberId)).thenReturn(List.of(mockTuple));

        List<StudyDtoResponse> result = studyService.findBookmarkStudies(memberId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBookmark()).isTrue();
        assertThat(result.get(0).getHeadCount()).isEqualTo(2L);
    }

    @Test
    void findBookmarkStudies_empty() {
        Mockito.when(studyRepository.findBookmarksByMemberId(memberId)).thenReturn(List.of());
        Mockito.when(studyRepository.findBookmarksMemberCountByMemberId(memberId)).thenReturn(List.of());

        List<StudyDtoResponse> result = studyService.findBookmarkStudies(memberId);

        assertThat(result).isEmpty();
    }

    // ── 스터디 단건 조회 ───────────────────────────────────

    @Test
    void findStudyById_withBookmark() {
        StudyBookmark mockBookmark = Mockito.mock(StudyBookmark.class);
        Mockito.when(studyRepository.findStudyById(studyId)).thenReturn(mockStudy);
        Mockito.when(studyMemberRepository.countStudyMemberByStudy(mockStudy)).thenReturn(3L);
        Mockito.when(studyBookmarkRepository.findStudyBookmarkByStudyIdAndMemberId(studyId, memberId)).thenReturn(mockBookmark);

        StudyDtoResponse result = studyService.findStudyById(mockMemberInfo, studyId);

        assertThat(result.getStudyId()).isEqualTo(studyId);
        assertThat(result.getBookmark()).isTrue();
        assertThat(result.getHeadCount()).isEqualTo(3L);
    }

    @Test
    void findStudyById_withoutBookmark() {
        Mockito.when(studyRepository.findStudyById(studyId)).thenReturn(mockStudy);
        Mockito.when(studyMemberRepository.countStudyMemberByStudy(mockStudy)).thenReturn(1L);
        Mockito.when(studyBookmarkRepository.findStudyBookmarkByStudyIdAndMemberId(studyId, memberId)).thenReturn(null);

        StudyDtoResponse result = studyService.findStudyById(mockMemberInfo, studyId);

        assertThat(result.getBookmark()).isFalse();
    }

    @Test
    void findStudyById_notFound_throwsException() {
        Mockito.when(studyRepository.findStudyById(999)).thenReturn(null);

        assertThatThrownBy(() -> studyService.findStudyById(mockMemberInfo, 999))
                .isInstanceOf(com.ssafy.interviewstudy.exception.message.NotFoundException.class);
    }

    @Test
    void findStudyById_deletedStudy_throwsException() {
        Study deletedStudy = Mockito.mock(Study.class);
        Mockito.when(deletedStudy.getIsDelete()).thenReturn(true);
        Mockito.when(studyRepository.findStudyById(studyId)).thenReturn(deletedStudy);

        assertThatThrownBy(() -> studyService.findStudyById(mockMemberInfo, studyId))
                .isInstanceOf(com.ssafy.interviewstudy.exception.message.NotFoundException.class);
    }

    // ── 스터디 상세 조회 ───────────────────────────────────

    @Test
    void findStudyDetailById() {
        Mockito.when(studyRepository.findStudyById(studyId)).thenReturn(mockStudy);

        StudyDetailDtoResponse result = studyService.findStudyDetailById(mockMemberInfo, studyId);

        assertThat(result.getStudyId()).isEqualTo(studyId);
        assertThat(result.getHeadCount()).isEqualTo(1);
    }

    @Test
    void findStudyDetailById_notFound_throwsException() {
        Mockito.when(studyRepository.findStudyById(999)).thenReturn(null);

        assertThatThrownBy(() -> studyService.findStudyDetailById(mockMemberInfo, 999))
                .isInstanceOf(com.ssafy.interviewstudy.exception.message.NotFoundException.class);
    }

    // ── 스터디 삭제 ────────────────────────────────────────

    @Test
    void removeStudy() {
        Study realStudy = Study.builder().id(studyId).build();
        Mockito.when(studyRepository.findById(studyId)).thenReturn(Optional.of(realStudy));

        studyService.removeStudy(studyId);

        assertThat(realStudy.getIsDelete()).isTrue();
        assertThat(realStudy.getLeader()).isNull();
        verify(studyMemberRepository, times(1)).deleteStudyMemberByStudy(realStudy);
    }

    @Test
    void removeStudy_notFound_throwsException() {
        Mockito.when(studyRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyService.removeStudy(999))
                .isInstanceOf(com.ssafy.interviewstudy.exception.study.NotFoundException.class);
    }

    // ── 스터디 수정 ────────────────────────────────────────

    @Test
    void modifyStudy() {
        Study realStudy = Study.builder().id(studyId).title("원래 제목").build();
        StudyDtoRequest modifyRequest = StudyDtoRequest.builder()
                .title("수정된 스터디")
                .description("수정 설명")
                .appliedJob("백엔드")
                .capacity(5)
                .recruitment(true)
                .deadline(LocalDateTime.now().plusDays(7))
                .tags(List.of(1))
                .build();
        StudyTagType mockTagType = StudyTagType.builder().id(1).tagName("백엔드").build();
        Mockito.when(studyRepository.findById(studyId)).thenReturn(Optional.of(realStudy));
        Mockito.when(studyTagTypeRepository.findById(1)).thenReturn(Optional.of(mockTagType));

        studyService.modifyStudy(studyId, modifyRequest);

        assertThat(realStudy.getTitle()).isEqualTo("수정된 스터디");
        verify(studyTagRepository, times(1)).deleteStudyTagByStudy(realStudy);
        verify(studyTagRepository, times(1)).save(any(StudyTag.class));
    }

    @Test
    void modifyStudy_notFound_throwsException() {
        Mockito.when(studyRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyService.modifyStudy(999, mockRequest))
                .isInstanceOf(com.ssafy.interviewstudy.exception.study.NotFoundException.class);
    }

    // ── 스터디 유효성 체크 ─────────────────────────────────

    @Test
    void checkStudy_activeStudy_returnsTrue() {
        Study realStudy = Study.builder().id(studyId).build();
        Mockito.when(studyRepository.findById(studyId)).thenReturn(Optional.of(realStudy));

        boolean result = studyService.checkStudy(studyId);

        assertThat(result).isTrue();
    }

    @Test
    void checkStudy_deletedStudy_returnsFalse() {
        Study deletedStudy = Study.builder().id(studyId).isDelete(true).build();
        Mockito.when(studyRepository.findById(studyId)).thenReturn(Optional.of(deletedStudy));

        boolean result = studyService.checkStudy(studyId);

        assertThat(result).isFalse();
    }

    @Test
    void checkStudy_notFound_returnsFalse() {
        Mockito.when(studyRepository.findById(999)).thenReturn(Optional.empty());

        boolean result = studyService.checkStudy(999);

        assertThat(result).isFalse();
    }
}
