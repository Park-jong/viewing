package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.StudyRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudyRequestRepository extends JpaRepository<StudyRequest, Integer> {

    @Query("select distinct sr from StudyRequest sr left join fetch sr.studyRequestFiles where sr.id = :id")
    public Optional<StudyRequest> findStudyRequestById(@Param("id") Integer id);
}
