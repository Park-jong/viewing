package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyMember;
import com.ssafy.interviewstudy.dto.study.StudyMemberDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Integer> {

    //멤버 수 카운팅
    public int countStudyMemberByStudy(Study study);

    //스터디 탈퇴
    @Modifying
    public void deleteStudyMemberByStudyIdAndMemberId(Integer studyId, Integer memberId);

    //스터디 탈퇴
    @Modifying
    public void deleteStudyMemberByStudyAndMember(Study study, Member member);

    //스터디 멤버인지
    public Optional<StudyMember> findByStudyAndMember(Study study, Member member);

    //스터디 멤버 목록
    @Query("select new com.ssafy.interviewstudy.dto.study.StudyMemberDto(sm.member, sm.isLeader) from StudyMember sm join sm.member m where sm.study.id = :studyId")
    public List<StudyMemberDto> findMembersByStudyId(@Param("studyId") Integer studyId);

    //스터디 멤버 목록
    @Query("select new com.ssafy.interviewstudy.dto.study.StudyMemberDto(sm.member, sm.isLeader) from StudyMember sm join sm.member m where sm.study = :study")
    public List<StudyMemberDto> findMembersByStudy(@Param("study") Study study);

    //스터디 삭제 시
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from StudyMember sm where sm.study.id = :id")
    public void deleteStudyMemberByStudyId(@Param("id") Integer studyId);

    //스터디 삭제 시
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from StudyMember sm where sm.study = :study")
    public void deleteStudyMemberByStudy(@Param("study")Study study);

}
