package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.CareerLevel;
import com.ssafy.interviewstudy.domain.study.Company;
import com.ssafy.interviewstudy.domain.study.Study;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@RequiredArgsConstructor
public class StudyRepositoryImpl implements StudyRepositoryCustom{

    @PersistenceContext
    EntityManager em;

    @Override
    //임시로 제작 수정 예정
    public List<Study> findStudiesBySearch(Company appliedCompany, String appliedJob, CareerLevel careerLevel) {
        StringBuffer queryString = new StringBuffer("select distinct s from Study s left join fetch s.studyTags st left join fetch st.tag t where s.isRecruit = true and s.isDelete = false");
        if(appliedCompany != null) queryString.append(" and s.appliedCompany = :appliedCompany");
        if(appliedJob != null && !appliedJob.equals("")) queryString.append(" and s.appliedJob like :appliedJob");
        if(careerLevel != null && careerLevel != CareerLevel.ALL) queryString.append(" and s.careerLevel = :careerLevel");

        TypedQuery<Study> query = em.createQuery(queryString.toString(), Study.class);

        if(appliedCompany != null) query.setParameter("appliedCompany", appliedCompany);
        if(appliedJob != null && !appliedJob.equals("")) query.setParameter("appliedJob", appliedJob);
        if(careerLevel != null && careerLevel != CareerLevel.ALL) query.setParameter("careerLevel", careerLevel);

        return query.getResultList();
    }
}
