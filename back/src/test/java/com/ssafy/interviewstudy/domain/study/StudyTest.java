package com.ssafy.interviewstudy.domain.study;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.repository.study.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(false)

class StudyTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyMemberRepository studyMemberRepository;

    @Autowired
    StudyRequestRepository studyRequestRepository;

    @Autowired
    StudyChatRepository studyChatRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    StudyCalendarRepository studyCalendarRepository;

//    @Test
//    public void StudyGetTest(){
//        Study study = new Study("title", "description", "job", 6);
//        studyRepository.save(study);
//        study.setCareerLevel(CareerLevel.INTERN);
//        em.flush();
//        em.clear();
//
//        assertThat(studyRepository.findById(1).get().getTitle()).isEqualTo("title");
//    }
//
//    @Test
//    public void StudySearchTest(){
//        Study study = new Study("title", "description", "job", 6);
//        studyRepository.save(study);
//        study.setCareerLevel(CareerLevel.INTERN);
//
//        Company c = new Company();
//        em.persist(c);
//        study.setAppliedCompany(c);
//
//        study.setAppliedJob("개발 및 유지 보수");
//        study.setRecruit(true);
//        em.flush();
//        em.clear();
//
//        Company company = companyRepository.findById(1).get();
//        List<Study> studiesBySearch = studyRepository.findStudiesBySearch(company, "%개발%", CareerLevel.ALL);
//        System.out.println(studiesBySearch.size());
//    }
//
//    @Test
//    public void StudyFindByRecruitTest(){
//        Study study1 = new Study("title", "description", "job", 6);
//        Study study2 = new Study("title", "description", "job", 6);
//        Study study3 = new Study("title", "description", "job", 6);
//        study1.setRecruit(true);
//        study2.setRecruit(true);
//        studyRepository.save(study1);
//        studyRepository.save(study2);
//        studyRepository.save(study3);
//
//        StudyTag st = new StudyTag();
//        em.persist(st);
//        StudyTagType stt = new StudyTagType();
//        em.persist(stt);
//        stt.setTagName("1111");
//
//        st.setTag(stt);
//
//        StudyTag st2 = new StudyTag();
//        em.persist(st2);
//        StudyTagType stt2 = new StudyTagType();
//        em.persist(stt2);
//        stt2.setTagName("2222");
//
//        st2.setTag(stt);
//
//        st.setStudy(study1);
//        st2.setStudy(study1);
//        em.flush();
//        em.clear();
//
//
//        List<Study> studiesByRecruit = studyRepository.findStudiesByRecruit();
//        System.out.println(studiesByRecruit.get(0).getStudyTags().get(0).getTag().getTagName());
//        assertThat(studyRepository.findStudiesByRecruit().size()).isEqualTo(2);
//    }
//
//    @Test
//    public void StudyRemoveTest(){
//        Study study1 = new Study("title", "description", "job", 6);
//        Study study2 = new Study("title", "description", "job", 6);
//        Study study3 = new Study("title", "description", "job", 6);
//
//        studyRepository.save(study1);
//        studyRepository.save(study2);
//        studyRepository.save(study3);
//        em.flush();
//        em.clear();
//
//        em.remove(studyRepository.findById(1).get());
//
//        assertThat(studyRepository.findAll().size()).isEqualTo(2);
//    }
//
//    @Test
//    public void StudyUpdateTest(){
//        Study study1 = new Study("title", "description", "job", 6);
//
//        studyRepository.save(study1);
//
//        study1.setTitle("title2");
//
//        em.flush();
//        em.clear();
//
//        assertThat(studyRepository.findById(1).get().getTitle()).isEqualTo("title2");
//    }
//
//    @Test
//    public void StudyMemberTest(){
//        Study study1 = new Study("title", "description", "job", 6);
//
//        studyRepository.save(study1);
//
//        Member member = new Member();
//
//        em.persist(member);
//
//        StudyMember sm = new StudyMember();
//        sm.setMember(member);
//        sm.setStudy(study1);
//        em.persist(sm);
//        em.flush();
//        em.clear();
//
//        studyMemberRepository.deleteStudyMemberId(1, 1);
//    }
//
//    @Test
//    public void updateRequest(){
//        Study study1 = new Study("title1", "description", "job", 6);
//        Study study2 = new Study("title2", "description", "job", 5);
//
//        studyRepository.save(study1);
//        studyRepository.save(study2);
//
//        Member member1 = new Member();
//        member1.setNickname("111");
//        em.persist(member1);
//        Member member2 = new Member();
//        member2.setNickname("222");
//        em.persist(member2);
//
//        StudyRequest sr1 = new StudyRequest();
//        sr1.setStudy(study1);
//        sr1.setApplicant(member1);
//        em.persist(sr1);
//        StudyRequest sr2 = new StudyRequest();
//        sr2.setStudy(study1);
//        sr2.setApplicant(member2);
//        em.persist(sr2);
//        em.flush();
//        em.clear();
//
//        StudyRequest sr = em.find(StudyRequest.class, 1);
//        Study find = studyRepository.findById(1).get();
//        List<StudyRequest> studyRequests = find.getStudyRequests();
//        StudyRequest srfind = studyRequests.get(0);
//
//        System.out.println(sr == srfind);
//
//    }
//
//    @Test
//    public void requestTest(){
//        Study study1 = new Study("title1", "description", "job", 6);
//        Study study2 = new Study("title2", "description", "job", 5);
//
//        studyRepository.save(study1);
//        studyRepository.save(study2);
//
//        Member member1 = new Member();
//        member1.setNickname("111");
//        em.persist(member1);
//        Member member2 = new Member();
//        member2.setNickname("222");
//        em.persist(member2);
//
//        StudyRequest sr1 = new StudyRequest();
//        sr1.setStudy(study1);
//        sr1.setApplicant(member1);
//        studyRequestRepository.save(sr1);
//        StudyRequest sr2 = new StudyRequest();
//        sr2.setStudy(study1);
//        sr2.setApplicant(member2);
//        studyRequestRepository.save(sr2);
//
//        StudyRequestFile srf = new StudyRequestFile();
//        srf.setRequest(sr1);
//        em.persist(srf);
//
//        em.flush();
//        em.clear();
//
//        System.out.println("--------------------------");
//
//        StudyRequest studyRequest = studyRequestRepository.findStudyRequestById(1).get();
//
//        int a = 1;
//
//    }
//
//    @Test
//    public void chatTest(){
//        //given
//        Member m1 = new Member();
//        m1.setNickname("1");
//        Member m2 = new Member();
//        m2.setNickname("2");
//        em.persist(m1);
//        em.persist(m2);
//
//        Study s1 = new Study();
//        s1.setTitle("title");
//        studyRepository.save(s1);
//
//        Study s2 = new Study();
//        s2.setTitle("title");
//        studyRepository.save(s2);
//
//        StudyMember sm1 = new StudyMember();
//        sm1.setStudy(s1);
//        sm1.setMember(m1);
//
//        studyMemberRepository.save(sm1);
//
//        StudyMember sm2 = new StudyMember();
//        sm2.setStudy(s1);
//        sm2.setMember(m2);
//
//        studyMemberRepository.save(sm2);
//
//        StudyChat sc1 = new StudyChat();
//        sc1.setAuthor(m1);
//        sc1.setContent("1");
//        sc1.setStudy(s1);
//        StudyChat sc2 = new StudyChat();
//        sc2.setAuthor(m1);
//        sc2.setContent("2");
//        sc2.setStudy(s1);
//        StudyChat sc3 = new StudyChat();
//        sc3.setAuthor(m2);
//        sc3.setContent("3");
//        sc3.setStudy(s1);
//        StudyChat sc4 = new StudyChat();
//        sc4.setAuthor(m1);
//        sc4.setContent("4");
//        sc4.setStudy(s1);
//        StudyChat sc5 = new StudyChat();
//        sc5.setAuthor(m2);
//        sc5.setContent("5");
//        sc5.setStudy(s1);
//        StudyChat sc6 = new StudyChat();
//        sc6.setAuthor(m1);
//        sc6.setContent("6");
//        sc6.setStudy(s1);
//        StudyChat sc7 = new StudyChat();
//        sc7.setAuthor(m1);
//        sc7.setContent("7");
//        sc7.setStudy(s1);
//        StudyChat sc8 = new StudyChat();
//        sc8.setAuthor(m2);
//        sc8.setContent("8");
//        sc8.setStudy(s1);
//        StudyChat sc9 = new StudyChat();
//        sc9.setAuthor(m1);
//        sc9.setContent("9");
//        sc9.setStudy(s1);
//        StudyChat sc10 = new StudyChat();
//        sc10.setAuthor(m2);
//        sc10.setContent("10");
//        sc10.setStudy(s1);
//        StudyChat sc11 = new StudyChat();
//        sc11.setAuthor(m1);
//        sc11.setContent("11");
//        sc11.setStudy(s1);
//        StudyChat sc12 = new StudyChat();
//        sc12.setAuthor(m1);
//        sc12.setContent("12");
//        sc12.setStudy(s1);
//        StudyChat sc13 = new StudyChat();
//        sc13.setAuthor(m2);
//        sc13.setContent("13");
//        sc13.setStudy(s1);
//        StudyChat sc14 = new StudyChat();
//        sc14.setAuthor(m1);
//        sc14.setContent("14");
//        sc14.setStudy(s1);
//        StudyChat sc15 = new StudyChat();
//        sc15.setAuthor(m1);
//        sc15.setContent("15");
//        sc15.setStudy(s1);
//
//        studyChatRepository.save(sc1);
//        studyChatRepository.save(sc2);
//        studyChatRepository.save(sc3);
//        studyChatRepository.save(sc4);
//        studyChatRepository.save(sc5);
//        studyChatRepository.save(sc6);
//        studyChatRepository.save(sc7);
//        studyChatRepository.save(sc8);
//        studyChatRepository.save(sc9);
//        studyChatRepository.save(sc10);
//        studyChatRepository.save(sc11);
//        studyChatRepository.save(sc12);
//        studyChatRepository.save(sc13);
//        studyChatRepository.save(sc14);
//        studyChatRepository.save(sc15);
//
//        em.flush();
//        em.clear();
//
//        //when
//        Study study = studyRepository.findById(1).get();
//        List<StudyChat> studyChats = study.getStudyChats();
//
//        //then
//        for (StudyChat studyChat : studyChats) {
//            System.out.println("-----------------------");
//            System.out.println(studyChat.getAuthor().getNickname() +" : " +studyChat.getContent());
//        }
//
//
//        StudyChat sc16 = new StudyChat();
//        sc16.setAuthor(m1);
//        sc16.setContent("16");
//        sc16.setStudy(s1);
//        StudyChat sc17 = new StudyChat();
//        sc17.setAuthor(m2);
//        sc17.setContent("17");
//        sc17.setStudy(s1);
//        StudyChat sc18 = new StudyChat();
//        sc18.setAuthor(m1);
//        sc18.setContent("18");
//        sc18.setStudy(s1);
//        StudyChat sc19 = new StudyChat();
//        sc19.setAuthor(m1);
//        sc19.setContent("19");
//        sc19.setStudy(s1);
//
//        studyChatRepository.save(sc16);
//        studyChatRepository.save(sc17);
//        studyChatRepository.save(sc18);
//        studyChatRepository.save(sc19);
//
//
//        List<StudyChat> studyChats2 = studyChatRepository.findNewStudyChats(study, 15);
//
//        //then
//        for (StudyChat studyChat : studyChats2) {
//            System.out.println("-----------------------");
//            System.out.println(studyChat.getAuthor().getNickname() +" : " +studyChat.getContent());
//        }
//    }
//
//    @Test
//    public void joinedStudyTest(){
//        //given
//        Member m1 = new Member();
//        Member m2 = new Member();
//
//        Study s1 = new Study();
//        Study s2 = new Study();
//        Study s3 = new Study();
//        Study s4 = new Study();
//        Study s5 = new Study();
//
//        em.persist(m1);
//        em.persist(m2);
//        studyRepository.save(s1);
//        studyRepository.save(s2);
//        studyRepository.save(s3);
//        studyRepository.save(s4);
//        studyRepository.save(s5);
//
//        StudyMember sm1 = new StudyMember();
//        StudyMember sm2 = new StudyMember();
//        StudyMember sm3 = new StudyMember();
//        StudyMember sm4 = new StudyMember();
//        StudyMember sm5 = new StudyMember();
//
//        sm1.setMember(m1);
//        sm2.setMember(m1);
//        sm3.setMember(m1);
//        sm4.setMember(m1);
//        sm5.setMember(m1);
//
//        sm1.setStudy(s1);
//        sm2.setStudy(s2);
//        sm3.setStudy(s3);
//        sm4.setStudy(s4);
//        sm5.setStudy(s5);
//
//        studyMemberRepository.save(sm1);
//        studyMemberRepository.save(sm2);
//        studyMemberRepository.save(sm3);
//        studyMemberRepository.save(sm4);
//        studyMemberRepository.save(sm5);
//
//        em.flush();
//        em.clear();
//
//        //when
//        Member member = em.find(Member.class, 1);
//        Member member2 = em.find(Member.class, 2);
//
//
//        //then
//        List<Study> studiesByMember = studyRepository.findStudiesByMember(member);
//        System.out.println(studiesByMember.size());
//
//        List<Study> studiesByMember2 = studyRepository.findStudiesByMember(member2);
//        System.out.println(studiesByMember2.size());
//
//    }
//
//    @Test
//    public void calendarTest(){
//        Study s = new Study();
//        StudyCalendar sc1 = new StudyCalendar();
//        StudyCalendar sc2 = new StudyCalendar();
//        StudyCalendar sc3 = new StudyCalendar();
//        StudyCalendar sc4 = new StudyCalendar();
//
//        studyRepository.save(s);
//        studyCalendarRepository.save(sc1);
//        studyCalendarRepository.save(sc2);
//        studyCalendarRepository.save(sc3);
//        studyCalendarRepository.save(sc4);
//
//        sc1.setStudy(s);
//        sc2.setStudy(s);
//        sc3.setStudy(s);
//        sc4.setStudy(s);
//
//        em.flush();
//        em.clear();
//
//        Study study = studyRepository.findById(1).get();
//        List<StudyCalendar> studyCalendersByStudy = studyCalendarRepository.findStudyCalendersByStudy(study);
//        System.out.println(studyCalendersByStudy.size());
//    }
//    @Test
//    public void findBySearch(){
//        Study s1 = new Study();
//        Study s2 = new Study();
//        Study s3 = new Study();
//        Study s4 = new Study();
//        Study s5 = new Study();
//        Study s6 = new Study();
//
//        s1.setIsRecruit(true);
//        s2.setIsRecruit(true);
//        s3.setIsRecruit(true);
//        s4.setIsRecruit(true);
//        s5.setIsRecruit(true);
//        s6.setIsRecruit(true);
//
//        s1.setIsDelete(false);
//        s2.setIsDelete(false);
//        s3.setIsDelete(false);
//        s4.setIsDelete(false);
//        s5.setIsDelete(false);
//        s6.setIsDelete(false);
//
//        StudyTag st1 = new StudyTag();
//        StudyTagType stt = new StudyTagType();
//        em.persist(st1);
//        em.persist(stt);
//        stt.setTagName("!");
//        st1.setTag(stt);
//        st1.setStudy(s1);
//
//
//        em.persist(s1);
//        em.persist(s2);
//        em.persist(s3);
//        em.persist(s4);
//        em.persist(s5);
//        em.persist(s6);
//
//        Company c1 = new Company();
//
//        em.persist(c1);
//
//        s1.setAppliedCompany(c1);
//        s2.setAppliedCompany(c1);
//        s3.setAppliedCompany(c1);
//        s4.setAppliedCompany(c1);
//
//        em.flush();
//        em.clear();
//
//        List<Study> result = studyRepository.findStudiesBySearch(null,null, null, null);
//
//        Assertions.assertThat(result.size()).isEqualTo(6);
//
//        result = studyRepository.findStudiesBySearch(true, c1.getId(), null, null);
//        Assertions.assertThat(result.size()).isEqualTo(4);
//
//    }
}