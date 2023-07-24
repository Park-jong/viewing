package com.ssafy.interviewstudy.repository.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyBookmark;
import com.ssafy.interviewstudy.repository.study.StudyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberStudyBookmarkRepositoryTest {

    @Autowired
    MemberStudyBookmarkRepository memberStudyBookmarkRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StudyRepository studyRepository;
    @Test
    @DisplayName("멤버가 북마크한 스터디들 조회 테스트")
    public void getBookmarkedStudyByMemberId(){
        //give
        //멤버 만들기
        Member member = Member.builder().email("ssafy1@gmail.com").build();
        Member member2 = Member.builder().email("ssafy2@gmail.com").build();
        Member member3 = Member.builder().email("ssafy3@gmail.com").build();

        //스터디 만들기
        Study study = Study.builder().build();
        Study study2 = Study.builder().build();
        Study study3 = Study.builder().build();

        //북마크 만들기
        StudyBookmark studyBookmark1 =
                StudyBookmark.builder().member(member).study(study).build();

        StudyBookmark studyBookmark2 =
                StudyBookmark.builder().member(member).study(study2).build();

    }

}
