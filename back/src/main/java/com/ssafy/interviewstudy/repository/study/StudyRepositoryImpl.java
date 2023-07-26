package com.ssafy.interviewstudy.repository.study;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssafy.interviewstudy.domain.study.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static com.ssafy.interviewstudy.domain.study.QCompany.company;
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
    public Page<Study> findStudiesBySearch(Boolean isRecruit, Integer appliedCompany, String appliedJob, CareerLevel careerLevel, Pageable pageable) {
        List<Study> result = queryFactory.select(study).distinct()
                .from(study)
                .join(study.appliedCompany, company).fetchJoin()
                .leftJoin(study.studyTags, studyTag).fetchJoin()
                .leftJoin(studyTag.tag, studyTagType).fetchJoin()
                .where(isRecruitTrue(isRecruit),
                        study.isDelete.eq(false),
                        appliedCompanyEq(appliedCompany),
                        appliedJobLike(appliedJob),
                        careerLevelEq(careerLevel))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(study.count())
                .from(study)
                .where(isRecruitTrue(isRecruit),
                        study.isDelete.eq(false),
                        appliedCompanyEq(appliedCompany),
                        appliedJobLike(appliedJob),
                        careerLevelEq(careerLevel))
                .fetchOne();
        return new PageImpl<>(result, pageable, count);
    }

    private BooleanExpression appliedCompanyEq(Integer appliedCompany){
        if(appliedCompany != null)
            return study.appliedCompany.id.eq(appliedCompany);
        return null;
    }

    private BooleanExpression appliedJobLike(String appliedJob){
        if(appliedJob != null && !appliedJob.isBlank())
            return study.appliedJob.like("%"+appliedJob+"%");
        return null;
    }

    private BooleanExpression careerLevelEq(CareerLevel careerLevel){
        if(careerLevel != null && careerLevel != CareerLevel.ALL)
            return study.careerLevel.eq(careerLevel);
        return null;
    }

    private BooleanExpression isRecruitTrue(Boolean isRecruit){
        if(isRecruit != null)
            return study.isRecruit.eq(true);
        return null;
    }
}