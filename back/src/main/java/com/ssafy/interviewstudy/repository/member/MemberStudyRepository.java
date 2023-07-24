package com.ssafy.interviewstudy.repository.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberStudyRepository extends JpaRepository<Study,Integer> {

    @Query("select s from Study s inner join s.studyMembers sm where sm.member.id=:id")
    List<Study> getStudyByMemberId(@Param("id")Integer memberId);

}
