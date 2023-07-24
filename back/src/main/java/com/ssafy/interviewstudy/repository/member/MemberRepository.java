package com.ssafy.interviewstudy.repository.member;

import com.ssafy.interviewstudy.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
}