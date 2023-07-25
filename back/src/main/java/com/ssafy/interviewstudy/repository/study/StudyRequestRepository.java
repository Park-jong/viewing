package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyRequestRepository extends JpaRepository<StudyRequest, Integer> {

    //신청 모두 조회
    @Query("select sr from StudyRequest sr join fetch sr.applicant where sr.study = :study")
    public List<StudyRequest> findStudyRequestsByStudy(@Param("study")Study study);

    //신청 각각 조회
    @Query("select distinct sr from StudyRequest sr join fetch sr.applicant left join fetch sr.studyRequestFiles where sr.id = :id")
    public Optional<StudyRequest> findStudyRequestById(@Param("id") Integer id);

    @Modifying(clearAutomatically = true)
    public void deleteStudyRequestById(Integer id);

    @Query("select sr from StudyRequest sr join fetch sr.applicant join fetch sr.study where sr.id = :id")
    public Optional<StudyRequest> findStudyAndMemberById(@Param("id")Integer id);
}
