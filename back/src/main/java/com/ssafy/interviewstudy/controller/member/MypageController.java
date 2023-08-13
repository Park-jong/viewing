package com.ssafy.interviewstudy.controller.member;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.dto.study.StudyDtoResponse;
import com.ssafy.interviewstudy.service.board.BoardService;
import com.ssafy.interviewstudy.service.member.MemberArticleService;
import com.ssafy.interviewstudy.service.member.MemberCommentService;
import com.ssafy.interviewstudy.service.study.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MypageController {

    private final MemberCommentService memberCommentService;

    private final BoardService boardService;

    private final StudyService studyService;

    private final MemberArticleService memberArticleService;

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member)
    @GetMapping(value="/members/{memberId}/article",params = "searchType=write")
    public ResponseEntity<?> getMyArticleList(@RequestParam("board")BoardType boardType,
                                              @PathVariable("memberId") Integer memberId,
                                              Pageable pageable){
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setMemberId(memberId);
        List<BoardResponse> articleByMemberId = memberArticleService.getArticleByMemberId(boardRequest, boardType, pageable);
        return ResponseEntity.ok().body(articleByMemberId);

    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member)
    @GetMapping(value="/members/{memberId}/article",params = "searchType=favor")
    public ResponseEntity<?> getFavorArticleList(@RequestParam("board")BoardType boardType, @PathVariable("memberId") Integer memberId){
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setMemberId(memberId);
        List<BoardResponse> boardResponseList = memberArticleService.getLikedArticleByMemberId(boardRequest,boardType);
        return ResponseEntity.ok().body(boardResponseList);

    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member)
    @GetMapping(value="/members/{memberId}/article",params = "searchType=comment")
    public ResponseEntity<?> getCommentedArticleList(@RequestParam("board")BoardType boardType, @PathVariable("memberId") Integer memberId){
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setMemberId(memberId);
        List<BoardResponse> boardResponseList = memberCommentService.getCommentedArticle(boardRequest,boardType);
        return ResponseEntity.ok().body(boardResponseList);

    }
    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member)
    @GetMapping(value="/members/{memberId}/studies")
    public ResponseEntity<?> getMyStudies(@PathVariable("memberId") Integer memberId){
        List<StudyDtoResponse> myStudies = studyService.findMyStudies(memberId);
        return ResponseEntity.ok().body(myStudies);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member)
    @GetMapping(value="/members/{memberId}/bookmarks/studies")
    public ResponseEntity<?> getMyBookmarkedStudies(@PathVariable("memberId") Integer memberId){
        List<StudyDtoResponse> myStudies = studyService.findBookmarkStudies(memberId);
        return ResponseEntity.ok().body(myStudies);
    }
}
