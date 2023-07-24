package com.ssafy.interviewstudy.repository.member;

import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.domain.study.StudyBookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberStudyBookmarkRepository extends JpaRepository<Study,Integer> {

    @Query("select s from Study s inner join s.studyBookmarks sb where sb.member.id = :id")
    public List<Study> getBookmarkedStudyByMemberId(@Param("id") Integer memberId);
}
