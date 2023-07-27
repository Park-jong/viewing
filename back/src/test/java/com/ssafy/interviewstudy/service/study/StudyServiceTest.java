package com.ssafy.interviewstudy.service.study;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.*;
import com.ssafy.interviewstudy.dto.study.*;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
@Rollback(value = false)
class StudyServiceTest {
    @PersistenceContext
    EntityManager em;

    @Autowired
    StudyService studyService;

    @Autowired
    StudyRepository studyRepository;

    @Autowired
    StudyTagRepository studyTagRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    StudyMemberRepository studyMemberRepository;

    PageRequest pageRequest = PageRequest.of(0, 5);

    @Autowired
    StudyRequestFileRepository studyRequestFileRepository;

    @Autowired
    StudyRequestRepository studyRequestRepository;

    @BeforeEach
    public void init(){
        Company c = Company.builder().name("삼성").build();
        em.persist(c);
        Company c2 = Company.builder().name("LG").build();
        em.persist(c2);

        Study study1 = Study.builder().title("test1").appliedCompany(c).isRecruit(true).isDelete(false).appliedJob("개발").build();
        Study study2 = Study.builder().title("test2").appliedCompany(c).isRecruit(true).isDelete(false).appliedJob("it").build();
        Study study3 = Study.builder().title("test3").appliedCompany(c).isRecruit(false).isDelete(false).appliedJob("it").build();
        Study study4 = Study.builder().title("test4").appliedCompany(c2).isRecruit(true).isDelete(false).appliedJob("개발").build();
        Study study5 = Study.builder().title("test5").appliedCompany(c2).isRecruit(false).isDelete(false).appliedJob("개발").build();

//        Study study6 = Study.builder().title("test4").appliedCompany(c2).isRecruit(true).isDelete(false).build();
//        Study study7 = Study.builder().title("test4").appliedCompany(c2).isRecruit(true).isDelete(false).build();
//        Study study8 = Study.builder().title("test4").appliedCompany(c2).isRecruit(true).isDelete(false).build();
//        Study study9 = Study.builder().title("test4").appliedCompany(c2).isRecruit(true).isDelete(false).build();
//        Study study10 = Study.builder().title("test4").appliedCompany(c2).isRecruit(true).isDelete(false).build();
//        Study study11 = Study.builder().title("test4").appliedCompany(c2).isRecruit(true).isDelete(false).build();
//        Study study12 = Study.builder().title("test4").appliedCompany(c2).isRecruit(true).isDelete(false).build();
//        Study study13 = Study.builder().title("test4").appliedCompany(c2).isRecruit(true).isDelete(false).build();
//        Study study14 = Study.builder().title("test4").appliedCompany(c2).isRecruit(true).isDelete(false).build();


        studyRepository.save(study1);
        studyRepository.save(study2);
        studyRepository.save(study3);
        studyRepository.save(study4);
        studyRepository.save(study5);

//        studyRepository.save(study6);
//        studyRepository.save(study7);
//        studyRepository.save(study8);
//        studyRepository.save(study9);
//        studyRepository.save(study10);
//        studyRepository.save(study11);
//        studyRepository.save(study12);
//        studyRepository.save(study13);
//        studyRepository.save(study14);

        StudyTagType stt1 = StudyTagType.builder().tagName("tag1").build();
        StudyTagType stt2 = StudyTagType.builder().tagName("tag2").build();
        StudyTagType stt3 = StudyTagType.builder().tagName("tag3").build();

        em.persist(stt1);
        em.persist(stt2);
        em.persist(stt3);

        StudyTag st1 = StudyTag.builder().study(study1).tag(stt1).build();
        StudyTag st2 = StudyTag.builder().study(study1).tag(stt2).build();
        StudyTag st3 = StudyTag.builder().study(study1).tag(stt3).build();
        StudyTag st4 = StudyTag.builder().study(study2).tag(stt1).build();
        StudyTag st5 = StudyTag.builder().study(study2).tag(stt2).build();
        StudyTag st6 = StudyTag.builder().study(study3).tag(stt2).build();
        StudyTag st7 = StudyTag.builder().study(study3).tag(stt3).build();
        StudyTag st8 = StudyTag.builder().study(study4).tag(stt1).build();
        StudyTag st9 = StudyTag.builder().study(study5).tag(stt2).build();

        studyTagRepository.save(st1);
        studyTagRepository.save(st2);
        studyTagRepository.save(st3);
        studyTagRepository.save(st4);
        studyTagRepository.save(st5);
        studyTagRepository.save(st6);
        studyTagRepository.save(st7);
        studyTagRepository.save(st8);
        studyTagRepository.save(st9);

        Member member1 = Member.builder().email("").nickname("q").build();
        Member member2 = Member.builder().email("").nickname("2").build();
        Member member3 = Member.builder().email("").nickname("3").build();
        Member member4 = Member.builder().email("").nickname("t").build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        memberRepository.save(member4);

        com.ssafy.interviewstudy.domain.study.StudyMember sm1 = com.ssafy.interviewstudy.domain.study.StudyMember.builder().study(study1).member(member1).build();
        com.ssafy.interviewstudy.domain.study.StudyMember sm2 = com.ssafy.interviewstudy.domain.study.StudyMember.builder().study(study1).member(member2).build();
        com.ssafy.interviewstudy.domain.study.StudyMember sm3 = com.ssafy.interviewstudy.domain.study.StudyMember.builder().study(study1).member(member3).build();
        com.ssafy.interviewstudy.domain.study.StudyMember sm4 = com.ssafy.interviewstudy.domain.study.StudyMember.builder().study(study1).member(member4).build();
        com.ssafy.interviewstudy.domain.study.StudyMember sm5 = com.ssafy.interviewstudy.domain.study.StudyMember.builder().study(study2).member(member1).build();
        com.ssafy.interviewstudy.domain.study.StudyMember sm6 = com.ssafy.interviewstudy.domain.study.StudyMember.builder().study(study2).member(member2).build();
        com.ssafy.interviewstudy.domain.study.StudyMember sm7 = com.ssafy.interviewstudy.domain.study.StudyMember.builder().study(study3).member(member3).build();
        com.ssafy.interviewstudy.domain.study.StudyMember sm8 = com.ssafy.interviewstudy.domain.study.StudyMember.builder().study(study3).member(member4).build();
        com.ssafy.interviewstudy.domain.study.StudyMember sm9 = com.ssafy.interviewstudy.domain.study.StudyMember.builder().study(study4).member(member2).build();
        com.ssafy.interviewstudy.domain.study.StudyMember sm10 = com.ssafy.interviewstudy.domain.study.StudyMember.builder().study(study5).member(member4).build();

        studyMemberRepository.save(sm1);
        studyMemberRepository.save(sm2);
        studyMemberRepository.save(sm3);
        studyMemberRepository.save(sm4);
        studyMemberRepository.save(sm5);
        studyMemberRepository.save(sm6);
        studyMemberRepository.save(sm7);
        studyMemberRepository.save(sm8);
        studyMemberRepository.save(sm9);
        studyMemberRepository.save(sm10);


        em.flush();
        em.clear();
    }

    @Test
    public void findMyStudyTest(){
        List<StudyDtoResponse> myStudies = studyService.findMyStudies(1);
        for (StudyDtoResponse myStudy : myStudies) {
            System.out.println(myStudy);
        }
    }

    @Test
    public void findBookmarkTest(){
        List<StudyDtoResponse> myStudies = studyService.findBookmarkStudies(1);
        for (StudyDtoResponse myStudy : myStudies) {
            System.out.println(myStudy);
        }
    }

    @Test
    public void findStudyTest(){

        StudyDtoResponse studyDtoResponse1 = studyService.findStudyById(1);
        StudyDtoResponse studyDtoResponse2 = studyService.findStudyById(2);
        StudyDtoResponse studyDtoResponse3 = studyService.findStudyById(3);
        StudyDtoResponse studyDtoResponse4 = studyService.findStudyById(4);

        Assertions.assertEquals(studyDtoResponse1.getTitle(), "test1");

    }

    @Test
    public void searchTest(){
        Page<StudyDtoResponse> result1 = studyService.findStudiesBySearch(null, 1, null, CareerLevel.ALL, pageRequest);
        Page<StudyDtoResponse> result2 = studyService.findStudiesBySearch(null, 2, null, CareerLevel.ALL, pageRequest);
        Assertions.assertEquals(result1.stream().count(), 3);
        Assertions.assertEquals(result2.stream().count(), 2);
        Page<StudyDtoResponse> result3 = studyService.findStudiesBySearch(true, 1, null, CareerLevel.ALL, pageRequest);
        Page<StudyDtoResponse> result4 = studyService.findStudiesBySearch(true, 2, null, CareerLevel.ALL, pageRequest);
        Assertions.assertEquals(result3.stream().count(), 2);
        Assertions.assertEquals(result4.stream().count(), 1);

        Page<StudyDtoResponse> result5 = studyService.findStudiesBySearch(null, 1, "i", CareerLevel.ALL, pageRequest);
        Assertions.assertEquals(result5.stream().count(), 2);


        Assertions.assertEquals(result3.getSize(), 5);
        Assertions.assertEquals(result3.getTotalElements(), 2);
        Assertions.assertEquals(result3.getTotalPages(), 1);
        Assertions.assertEquals(result4.hasNext(), false);

    }

    @Test
    public void allStudies(){
        Page<StudyDtoResponse> result1 = studyService.findStudies(null, pageRequest);
        Assertions.assertEquals(result1.stream().count(), 5);

        Page<StudyDtoResponse> result2 = studyService.findStudies(true, pageRequest);
        Assertions.assertEquals(result2.stream().count(), 3);
    }

    @Test
    public void saveTest(){
        Member m = Member.builder().nickname("saveTest").email("@gmail.com").build();
        memberRepository.save(m);
        StudyDtoRequest s = StudyDtoRequest.builder().appliedCompany(1).title("save").leaderId(m.getId()).description("des").build();
        List<Integer> tags = new ArrayList<>();
        tags.add(1);
        tags.add(2);
        s.setTags(tags);

        Integer id = studyService.addStudy(s);

        em.flush();
        em.clear();

        List<StudyMemberDto> result = studyMemberRepository.findMembersByStudyId(id);
        Assertions.assertEquals(result.get(0).getId(), m.getId());
    }

    @Test
    public void deleteTest(){
        Study study = studyRepository.findById(1).get();
        studyService.removeStudy(study.getId());
    }

    @Test
    public void updateTest(){
        Member m = Member.builder().nickname("saveTest").email("@gmail.com").build();
        memberRepository.save(m);
        StudyDtoRequest s = StudyDtoRequest.builder().appliedCompany(1).title("save").leaderId(m.getId()).description("des").build();
        List<Integer> tags = new ArrayList<>();
        tags.add(1);
        tags.add(2);
        s.setTags(tags);

        Integer id = studyService.addStudy(s);

        em.flush();
        em.clear();

        Study study = studyRepository.findById(6).get();
        List<Integer> tags2 = new ArrayList<>();
        s = StudyDtoRequest.builder().capacity(100).build();
        tags2.add(2);
        tags2.add(3);
        s.setTags(tags2);
        s.setTags(tags2);
        studyService.modifyStudy(study.getId(), s);
    }

    @Test
    public void addRequestTest(){
        Study s = studyRepository.findById(1).get();
        RequestDto requestDto = new RequestDto();
        requestDto.setUserId(1);
        requestDto.setContent("1232313213131");

        studyService.addRequest(s.getId(), requestDto);


    }

    @Test
    public void requestTest(){
        Study s = studyRepository.findById(1).get();
        RequestDto requestDto = new RequestDto();
        requestDto.setUserId(1);
        requestDto.setContent("1232313213131");
        RequestDto requestDto2 = new RequestDto();
        requestDto2.setUserId(2);
        requestDto2.setContent("1232313213131");
        RequestDto requestDto3 = new RequestDto();
        requestDto3.setUserId(3);
        requestDto3.setContent("1232313213131");
        RequestDto requestDto4 = new RequestDto();
        requestDto4.setUserId(4);
        requestDto4.setContent("1232313213131");

        requestDto4.getRequestFiles().add(new RequestFile());
        requestDto4.getRequestFiles().add(new RequestFile());
        requestDto3.getRequestFiles().add(new RequestFile());
        requestDto2.getRequestFiles().add(new RequestFile());

        studyService.addRequest(s.getId(), requestDto);
        studyService.addRequest(s.getId(), requestDto2);
        studyService.addRequest(s.getId(), requestDto3);
        studyService.addRequest(s.getId(), requestDto4);


        em.flush();
        em.clear();

        List<RequestDtoResponse> result = studyService.findRequestsByStudy(1);

        RequestDtoResponse re = studyService.findRequestById(4);

        Assertions.assertEquals(result.size(), 4);
        Assertions.assertEquals(re.getRequestFiles().size(), 2);

        studyService.permitRequest(1, 1, 1);
        studyService.permitRequest(2, 1, 2);
        studyService.rejectRequest(3, 1, 3);
        studyService.rejectRequest(4, 1, 4);

        result = studyService.findRequestsByStudy(1);
        Assertions.assertEquals(result.size(), 0);
    }

    @Test
    public void leaveTest(){
        studyService.leaveStudy(1, 1);
    }

    @Test
    public void leaderTest(){
        studyService.delegateLeader(1, 1, 2);
        System.out.println(studyRepository.findStudyById(1).getLeader().getId());
        studyService.delegateLeader(1, 2, 3);
        System.out.println(studyRepository.findStudyById(1).getLeader().getId());
    }
    @Test
    public void memberListTest(){
        List<StudyMemberDto> result = studyService.findStudyMembers(1);
        System.out.println(result);

    }

    @Test
    public void studyChatTest(){
        ChatRequest cr1 = ChatRequest.builder().userId(1).content("내용1").build();
        ChatRequest cr2 = ChatRequest.builder().userId(2).content("내용2").build();
        ChatRequest cr3 = ChatRequest.builder().userId(1).content("내용3").build();
        ChatRequest cr4 = ChatRequest.builder().userId(2).content("내용4").build();
        studyService.addChat(1, cr1);
        studyService.addChat(1, cr2);
        studyService.addChat(1, cr3);
        studyService.addChat(1, cr4);

        List<ChatResponse> studyChats = studyService.findStudyChats(1, null);

        Assertions.assertEquals(studyChats.size(), 4);

        ChatRequest cr5 = ChatRequest.builder().userId(1).content("내용1").build();
        ChatRequest cr6 = ChatRequest.builder().userId(1).content("내용1").build();
        ChatRequest cr7 = ChatRequest.builder().userId(1).content("내용1").build();

        studyService.addChat(1, cr5);
        studyService.addChat(1, cr6);
        studyService.addChat(1, cr7);

        List<ChatResponse> studyChats1 = studyService.findStudyChats(1, 4);

        Assertions.assertEquals(studyChats1.size(), 3);

    }

    @Test
    public void calendarTest(){
        studyService.addStudyCalendar(1, StudyCalendarDtoRequest.builder().userId(1).build());
        studyService.addStudyCalendar(1, StudyCalendarDtoRequest.builder().userId(1).build());
        studyService.addStudyCalendar(1, StudyCalendarDtoRequest.builder().userId(1).build());
        studyService.addStudyCalendar(1, StudyCalendarDtoRequest.builder().userId(1).build());

        List<StudyCalendarDtoResponse> result = studyService.findStudyCalenarByStudy(1);

        studyService.modifyStudyCalendar(1, 1, StudyCalendarDtoRequest.builder().userId(1).description("12345").build());

        studyService.removeStudyCalendar(1, 2);

        List<StudyCalendarDtoResponse> result2 = studyService.findStudyCalenarByStudy(1);

        for (StudyCalendarDtoResponse studyCalendarDtoResponse : result2) {
            System.out.println(studyCalendarDtoResponse);
        }

    }
}