package com.ssafy.interviewstudy.service.member.bookmark;

import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkRequest;
import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkResponse;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;

public interface MemberStudyBookmarkService {
    @Transactional
    StudyBookmarkResponse createStudyBookmark(@Valid StudyBookmarkRequest studyBookmarkRequest);

    @Transactional
    void deleteStudyBookmark(@Valid StudyBookmarkRequest studyBookmarkRequest);

    @Transactional
    Boolean checkStudyBookmarkByMemberId(Integer memberId, Integer studyId);
}
