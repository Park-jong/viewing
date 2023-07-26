package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudyRepository extends JpaRepository<Study, Integer>, StudyRepositoryCustom {

    @Query("select distinct s from Study s join fetch s.appliedCompany left join fetch s.studyTags st left join fetch st.tag t where s.id = :id and s.isDelete = false")
    //스터디 하나 조회
    public Study findStudyById(@Param("id") Integer id);

    @Query("select distinct s from Study s join s.studyMembers sm join fetch s.appliedCompany left join fetch s.studyTags st left join fetch st.tag where sm.member = :member")
    public List<Study> findStudiesByMember(@Param("member") Member member);

    @Query("select distinct s from Study s join s.studyBookmarks sb join fetch s.appliedCompany left join fetch s.studyTags st left join fetch st.tag where sb.member = :member")
    public List<Study> findBookmarksByMember(@Param("member")Member member);
}
