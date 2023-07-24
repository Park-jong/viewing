package com.ssafy.interviewstudy.repository.study;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.interviewstudy.domain.study.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static com.ssafy.interviewstudy.domain.study.QStudy.study;
import static com.ssafy.interviewstudy.domain.study.QStudyTag.*;
import static com.ssafy.interviewstudy.domain.study.QStudyTagType.*;

@RequiredArgsConstructor
@Repository
public class StudyRepositoryImpl implements StudyRepositoryCustom{

    EntityManager em;

    JPAQueryFactory queryFactory;

    @Autowired
    public StudyRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    //임시로 제작 수정 예정
    public List<Study> findStudiesBySearch(Company appliedCompany, String appliedJob, CareerLevel careerLevel) {
        List<Study> result = queryFactory.select(study).distinct()
                .from(study)
                .leftJoin(studyTag.study).fetchJoin()
                .leftJoin(studyTag.tag, studyTagType).fetchJoin()
                .where(study.isRecruit.isTrue(),
                        study.isDelete.isFalse(),
                        appliedCompanyEq(appliedCompany),
                        appliedJobLike(appliedJob),
                        careerLevelEq(careerLevel))
                .fetch();
//        StringBuffer queryString = new StringBuffer("select distinct s from Study s left join fetch s.studyTags st left join fetch st.tag t where s.isRecruit = true and s.isDelete = false");
//        if(appliedCompany != null) queryString.append(" and s.appliedCompany = :appliedCompany");
//        if(appliedJob != null && !appliedJob.equals("")) queryString.append(" and s.appliedJob like :appliedJob");
//        if(careerLevel != null && careerLevel != CareerLevel.ALL) queryString.append(" and s.careerLevel = :careerLevel");
//
//        TypedQuery<Study> query = em.createQuery(queryString.toString(), Study.class);
//
//        if(appliedCompany != null) query.setParameter("appliedCompany", appliedCompany);
//        if(appliedJob != null && !appliedJob.equals("")) query.setParameter("appliedJob", appliedJob);
//        if(careerLevel != null && careerLevel != CareerLevel.ALL) query.setParameter("careerLevel", careerLevel);
//
        return result;
    }

    private BooleanExpression appliedCompanyEq(Company appliedCompany){
        if(appliedCompany != null)
            return study.appliedCompany.eq(appliedCompany);
        return null;
    }

    private BooleanExpression appliedJobLike(String appliedJob){
        if(appliedJob != null && !appliedJob.isBlank())
            return study.appliedJob.like(appliedJob);
        return null;
    }

    private BooleanExpression careerLevelEq(CareerLevel careerLevel){
        if(careerLevel != null)
            return study.careerLevel.eq(careerLevel);
        return null;
    }
}
