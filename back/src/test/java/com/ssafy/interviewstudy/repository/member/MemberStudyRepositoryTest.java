package com.ssafy.interviewstudy.repository.member;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyMember;
import com.ssafy.interviewstudy.repository.study.StudyMemberRepository;
import com.ssafy.interviewstudy.repository.study.StudyRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@SpringBootTest
@Transactional
public class MemberStudyRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    MemberStudyRepository memberStudyRepository;

    @Autowired
    StudyMemberRepository studyMemberRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    @DisplayName("멤버 PK로 자신이 가입한 스터디원 조회")
    public void findStudyMembersByMemberIdTest(){

        //give

        //테스트에 사용할 스터디 생성
        Study createdStudy1
                = Study.builder().capacity(5).build();

        Study createdStudy2
                = Study.builder().capacity(5).build();

        Study createdStudy3
                = Study.builder().capacity(5).build();

        studyRepository.save(createdStudy1);
        studyRepository.save(createdStudy2);
        studyRepository.save(createdStudy3);

        //테스트에 사용할 스터디 멤버 생성
        Member createdMember1  = Member.builder().email("ssafy1@ssafy.com").build();
        Member createdMember2  = Member.builder().email("ssafy2@ssafy.com").build();
        Member createdMember3  = Member.builder().email("ssafy3@ssafy.com").build();

        memberRepository.save(createdMember1);
        memberRepository.save(createdMember2);
        memberRepository.save(createdMember3);

        //테스트에 사용할 스터디-멤버 객체 생성(스터디에 속한 멤버)
        StudyMember study1Member1 = StudyMember.builder().member(createdMember1).study(createdStudy1).build();
        StudyMember study2Member1 = StudyMember.builder().member(createdMember1).study(createdStudy2).build();

        studyMemberRepository.save(study1Member1);
        studyMemberRepository.save(study2Member1);

        //when
        List<Study> studyList =
                memberStudyRepository.getStudyByMemberId(createdMember1.getId());

        org.assertj.core.api.Assertions.assertThat(studyList)
                .contains(
                        createdStudy1
                ).contains(
                        createdStudy2
                );

    }
}
