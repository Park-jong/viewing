package com.ssafy.interviewstudy.service.member.bookmark;

import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkRequest;
import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkResponse;

import javax.validation.Valid;

public interface MemberStudyBookmarkService {
    StudyBookmarkResponse createStudyBookmark(@Valid StudyBookmarkRequest studyBookmarkRequest);

    void deleteStudyBookmark(@Valid StudyBookmarkRequest studyBookmarkRequest);

    Boolean checkStudyBookmarkByMemberId(Integer memberId, Integer studyId);
}
