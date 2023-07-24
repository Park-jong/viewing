package com.ssafy.interviewstudy.repository.member;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.StudyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberStudyRepository extends JpaRepository<StudyMember,Integer> {

    List<StudyMember> findStudyMembersByMemberId(Integer memberId);

}
