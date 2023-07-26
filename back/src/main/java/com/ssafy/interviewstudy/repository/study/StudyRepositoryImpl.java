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
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

import static com.ssafy.interviewstudy.domain.study.QCompany.company;
import static com.ssafy.interviewstudy.domain.study.QStudy.study;
import static com.ssafy.interviewstudy.domain.study.QStudyTag.*;
import static com.ssafy.interviewstudy.domain.study.QStudyTagType.*;

@Repository
public class StudyRepositoryImpl implements StudyRepositoryCustom{

    private final EntityManager em;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public StudyRepositoryImpl(EntityManager em) {
        this.em = em;
        queryFactory = new JPAQueryFactory(em);
    }

    @Override
    //조건에 따라 study 검색 결과
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
        return appliedCompany != null ? study.appliedCompany.id.eq(appliedCompany) : null;
    }

    private BooleanExpression appliedJobLike(String appliedJob){
        return StringUtils.hasText(appliedJob) ? study.appliedJob.like("%"+appliedJob+"%") : null;
    }

    private BooleanExpression careerLevelEq(CareerLevel careerLevel){
        return (careerLevel != null && careerLevel != CareerLevel.ALL) ? study.careerLevel.eq(careerLevel) : null;
    }

    private BooleanExpression isRecruitTrue(Boolean isRecruit){
        return isRecruit != null ? study.isRecruit.eq(true) : null;
    }
}
