package com.ssafy.interviewstudy.repository.study;

import com.ssafy.interviewstudy.domain.study.StudyRequest;
import com.ssafy.interviewstudy.domain.study.StudyRequestFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudyRequestFileRepository extends JpaRepository<StudyRequestFile, Integer> {
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from StudyRequestFile srf where srf.request.id = :id")
    public void deleteByRequestId(@Param("id") Integer id);
}
