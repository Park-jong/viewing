package com.ssafy.interviewstudy.controller.board;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.service.board.BoardService;
import com.ssafy.interviewstudy.service.board.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members/{memberId}/likes")
public class BoardLikeController {

    private final BoardService boardService;
    private final CommentService commentService;

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Article_Like)
    @PostMapping("/boards/{articleId}")
    public ResponseEntity<?> articleLikeSave(@PathVariable Integer memberId, @PathVariable Integer articleId){
        Integer likeId = boardService.saveArticleLike(memberId, articleId);

        return ResponseEntity.ok(likeId);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Article_Like)
    @DeleteMapping("/boards/{articleId}")
    public ResponseEntity<?> articleLikeRemove(@PathVariable Integer memberId, @PathVariable Integer articleId){
        boardService.removeArticleLike(memberId, articleId);

        return ResponseEntity.ok().build();
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Comment_Like)
    @PostMapping("comments/{commentId}")
    public ResponseEntity<?> commentLikeSave(@PathVariable Integer memberId, @PathVariable Integer commentId){
        Integer likeId = commentService.saveCommentLike(memberId, commentId);

        return ResponseEntity.ok(likeId);
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Comment_Like)
    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<?> commentLikeRemove(@PathVariable Integer memberId, @PathVariable Integer commentId){
        commentService.removeCommentLike(memberId, commentId);

        return ResponseEntity.ok().build();
    }
}
