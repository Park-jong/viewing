package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.domain.study.Company;
import com.ssafy.interviewstudy.domain.study.Study;

import java.util.List;


public interface StudyRepositoryCustom {
    //조건으로 조회
    List<Study> findStudiesBySearch(Company appliedCompany, String appliedJob, CareerLevel careerLevel);
}
