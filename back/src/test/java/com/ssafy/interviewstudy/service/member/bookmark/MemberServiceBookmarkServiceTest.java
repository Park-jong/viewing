package com.ssafy.interviewstudy.service.member.bookmark;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Company;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyBookmark;
import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkRequest;
import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkResponse;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.member.MemberStudyBookmarkRepository;
import com.ssafy.interviewstudy.repository.study.CompanyRepository;
import com.ssafy.interviewstudy.repository.study.StudyRepository;
import com.ssafy.interviewstudy.service.member.bookmark.MemberStudyBookmarkService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@Transactional
public class MemberServiceBookmarkServiceTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    MemberStudyBookmarkService memberStudyBookmarkService;

    @Autowired
    MemberStudyBookmarkRepository memberStudyBookmarkRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Test
    @DisplayName("스터디 북마크 생성 테스트")
    public void createStudyBookmarkTest(){
        //give
        //북마크할 멤버
        Member memberA = Member.builder().email("tkdwo7699@gmail.com").build();
        Member memberB = Member.builder().email("sj_1333@naver.com").build();

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //지원회사 (스터디에 필수!)
        Company company = Company.builder().name("Sandman").build();
        companyRepository.save(company);

        //북마크 대상 스터디
        Study studyA = Study.builder().isDelete(false).appliedCompany(company).build();
        Study studyB = Study.builder().isDelete(false).appliedCompany(company).build();
        Study studyC = Study.builder().isDelete(false).appliedCompany(company).build();

        studyRepository.save(studyA);
        studyRepository.save(studyB);
        studyRepository.save(studyC);

        //when
        
        //멤버 A가 스터디 A에 북마크
        StudyBookmarkRequest studyBookmarkRequest =
                StudyBookmarkRequest
                        .builder()
                        .studyId(studyA.getId())
                        .memberId(memberA.getId())
                        .build();


        memberStudyBookmarkService.createStudyBookmark(studyBookmarkRequest);

        //멤버 A가 스터디 B에 북마크
        StudyBookmarkRequest studyBookmarkRequest2 =
                StudyBookmarkRequest
                        .builder()
                        .studyId(studyB.getId())
                        .memberId(memberA.getId())
                        .build();
        StudyBookmarkResponse studyBookmarkResponse =
                memberStudyBookmarkService.createStudyBookmark(studyBookmarkRequest2);

        //then
        //멤버 A가 북마크한 스터디를 가져옴
        List<Study> studyList = memberStudyBookmarkRepository.getBookmarkedStudyByMemberId(memberA.getId());

        //멤버 B가 북마크한 스터디를 가져옴
        List<Study> studyList2 = memberStudyBookmarkRepository.getBookmarkedStudyByMemberId(memberB.getId());

        assertThat(studyList).hasSize(2);
        assertThat(studyList2).hasSize(0);
    }

    @Test
    @DisplayName("스터디 북마크 삭제 테스트")
    public void deleteStudyBookmarkTest(){
        //give
        //북마크할 멤버
        Member memberA = Member.builder().email("tkdwo7699@gmail.com").build();
        Member memberB = Member.builder().email("sj_1333@naver.com").build();

        memberRepository.save(memberA);

        //지원회사 (스터디에 필수!)
        Company company = Company.builder().name("Sandman").build();
        companyRepository.save(company);

        //북마크 대상 스터디
        Study studyA = Study.builder().isDelete(false).appliedCompany(company).build();
        Study studyB = Study.builder().isDelete(false).appliedCompany(company).build();
        Study studyC = Study.builder().isDelete(false).appliedCompany(company).build();

        studyRepository.save(studyA);
        studyRepository.save(studyB);
        studyRepository.save(studyC);

        //멤버 A가 스터디 A에 북마크
        StudyBookmarkRequest studyBookmarkRequest =
                StudyBookmarkRequest
                        .builder()
                        .studyId(studyA.getId())
                        .memberId(memberA.getId())
                        .build();


        StudyBookmarkResponse studyBookmarkResponse =
                memberStudyBookmarkService.createStudyBookmark(studyBookmarkRequest);

        //생성한 스터디 아이디
        Integer studyBookmarkId = studyBookmarkResponse.getStudyBookmarkId();

        assertThat(studyBookmarkId).isNotNull();

        //when

        memberStudyBookmarkService.deleteStudyBookmark(studyBookmarkRequest);

       StudyBookmark studyBookmark =
               memberStudyBookmarkRepository.findStudyBookmarkByStudyIdAndMemberId(
                       studyBookmarkRequest.getStudyId(),
                       studyBookmarkRequest.getMemberId()
               );
       assertThat(studyBookmark).isNull();
    }
}
