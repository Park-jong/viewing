package com.ssafy.interviewstudy.controller.member.bookmark;

import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkRequest;
import com.ssafy.interviewstudy.dto.member.bookmark.StudyBookmarkResponse;
import com.ssafy.interviewstudy.service.member.bookmark.MemberStudyBookmarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/users/{userId}/studies/{studyId}/bookmark")
public class MemberBookmarkController {

    private MemberStudyBookmarkService memberStudyBookmarkService;

    @Autowired
    public MemberBookmarkController(MemberStudyBookmarkService memberStudyBookmarkService) {
        this.memberStudyBookmarkService = memberStudyBookmarkService;
    }

    @PostMapping
    public ResponseEntity<?> createStudyBookmark(@PathVariable("userId") Integer memberId, @PathVariable("studyId") Integer studyId){
        StudyBookmarkResponse studyBookmarkResponse =
                memberStudyBookmarkService.createStudyBookmark(
                        StudyBookmarkRequest
                                .builder()
                                .memberId(memberId)
                                .studyId(studyId)
                                .build()

                );

        return ResponseEntity.ok().body(studyBookmarkResponse);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteStudyBookmark(@PathVariable("userId") Integer memberId, @PathVariable("studyId") Integer studyId){
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
