package com.ssafy.interviewstudy.controller.member.bookmark;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkRequest;
import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkResponse;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.service.member.bookmark.MemberStudyBookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/members/{memberId}/studies/{studyId}/bookmark")
public class MemberBookmarkController {

    private MemberStudyBookmarkService memberStudyBookmarkService;

    @Autowired
    public MemberBookmarkController(MemberStudyBookmarkService memberStudyBookmarkService) {
        this.memberStudyBookmarkService = memberStudyBookmarkService;
    }

    @JWTRequired(required = true)
    @PostMapping
    public ResponseEntity<?> createStudyBookmark(@MemberInfo JWTMemberInfo jwtMemberInfo, @PathVariable("studyId") Integer studyId){
        StudyBookmarkResponse studyBookmarkResponse =
                memberStudyBookmarkService.createStudyBookmark(
                        StudyBookmarkRequest
                                .builder()
                                .memberId(jwtMemberInfo.getMemberId())
                                .studyId(studyId)
                                .build()

                );

        return ResponseEntity.ok().body(studyBookmarkResponse);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Study_Bookmark)
    @DeleteMapping
    public ResponseEntity<?> deleteStudyBookmark(@PathVariable("memberId") Integer memberId, @PathVariable("studyId") Integer studyId){
        memberStudyBookmarkService.deleteStudyBookmark(
                StudyBookmarkRequest
                        .builder()
                        .memberId(memberId)
                        .studyId(studyId)
                        .build()

        );
        return ResponseEntity.ok().build();
    }

}
