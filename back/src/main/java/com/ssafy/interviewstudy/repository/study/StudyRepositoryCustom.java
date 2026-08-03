package com.ssafy.interviewstudy.repository.study;

import com.querydsl.core.Tuple;
import com.ssafy.interviewstudy.domain.study.CareerLevel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudyRepositoryCustom {
    //조건으로 조회
    Page<Tuple> findStudiesBySearch(Boolean isRecruit, String appliedCompany, String appliedJob, CareerLevel careerLevel, Integer tag, Pageable pageable);

    List<Tuple> findBookmarksMemberCountByMemberId(Integer memberId);

    List<Tuple> findMyStudyMemberCountByMemberId(Integer memberId);

    List<Tuple> isBookmark(Integer memberId, List<Integer> studyIds);
}
