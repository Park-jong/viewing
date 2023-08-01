package com.ssafy.interviewstudy.controller.board;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.annotation.MemberInfo;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.CommentResponse;
import com.ssafy.interviewstudy.dto.member.jwt.JWTMemberInfo;
import com.ssafy.interviewstudy.service.board.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/boards/{articleId}/comments")
@RestController
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    // 댓글 조회
    // 로그인 안했으면 isLike null 인 상태로 반환
    @JWTRequired
    @GetMapping
    public ResponseEntity<?> commentList(@MemberInfo JWTMemberInfo memberInfo, @PathVariable Integer articleId){
        List<CommentResponse> commentResponses;
        if(memberInfo != null) commentResponses = commentService.findComments(memberInfo.getMemberId(), articleId);
        else commentResponses = commentService.findComments(null, articleId);
        return ResponseEntity.ok(commentResponses);
    }

    // 댓글 저장
    @JWTRequired(required = true)
    @PostMapping
    public ResponseEntity<?> commentSave(@PathVariable Integer articleId,
                                         @MemberInfo JWTMemberInfo memberInfo,
                                         @RequestBody CommentRequest commentRequest){
        commentRequest.setMemberId(memberInfo.getMemberId());
        Integer commentId = commentService.saveComment(articleId, commentRequest);

        return ResponseEntity.ok(commentId);
    }

    // 댓글 수정
    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Comment)
    @PutMapping("/{commentId}")
    public ResponseEntity<?> commentModify(@PathVariable Integer commentId,
                                           @MemberInfo JWTMemberInfo memberInfo,
                                           @RequestBody CommentRequest commentRequest){
        commentRequest.setMemberId(memberInfo.getMemberId());
        CommentResponse commentResponse = commentService.modifyComment(commentId, commentRequest);

        return ResponseEntity.ok(commentResponse);
    }

    // 댓글 삭제
    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Comment)
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> commentRemove(@PathVariable Integer commentId){
        commentService.removeComment(commentId);
        return ResponseEntity.ok().build();
    }

    // 대댓글 작성
    @JWTRequired(required = true)
    @PostMapping("/{commentId}/replies")
    public ResponseEntity<?> replySave(@PathVariable Integer articleId,
                                       @PathVariable Integer commentId,
                                       @MemberInfo JWTMemberInfo memberInfo,
                                       @RequestBody CommentRequest commentRequest){
        commentRequest.setMemberId(memberInfo.getMemberId());
        Integer replyId = commentService.saveCommentReply(articleId, commentId, commentRequest);

        return  ResponseEntity.ok(replyId);
    }
}
