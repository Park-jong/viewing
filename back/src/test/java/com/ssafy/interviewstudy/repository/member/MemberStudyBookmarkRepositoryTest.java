package com.ssafy.interviewstudy.repository.member;

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
    }

}
