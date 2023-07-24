package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StudyMemberRepository extends JpaRepository<StudyMember, Integer> {

    //멤버 수 카운팅
    public int countStudyMemberByStudy(Study study);

    //스터디 탈퇴
    @Modifying(clearAutomatically = true)
    @Query("delete from StudyMember sm where sm.study.id = :study_id and sm.member.id = :member_id")
    public void deleteStudyMemberId(@Param("study_id") Integer studyId, @Param("member_id") Integer memberId);

    //스터디 탈퇴
    @Modifying(clearAutomatically = true)
    @Query("delete from StudyMember sm where sm.study = :study and sm.member = :member")
    public void deleteStudyMember(@Param("study") Study study, @Param("member") Member member);
}
