package com.ssafy.interviewstudy.repository.study;

import com.querydsl.core.Tuple;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.domain.study.Company;
import com.ssafy.interviewstudy.domain.study.Study;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface StudyRepositoryCustom {
    //조건으로 조회
    Page<Tuple> findStudiesBySearch(Boolean isRecruit, Integer appliedCompany, String appliedJob, CareerLevel careerLevel, Integer memberId, Pageable pageable);

    List<Tuple> findBookmarksMemberCountByMember(Member member);

    List<Tuple> findMyStudyMemberCountByMember(Member member);

    }
