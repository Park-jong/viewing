package com.ssafy.interviewstudy.controller.board;

import com.ssafy.interviewstudy.annotation.Authority;
import com.ssafy.interviewstudy.annotation.AuthorityType;
import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.service.redis.ArticleLikeService;
import com.ssafy.interviewstudy.service.redis.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members/{memberId}/likes")
public class BoardLikeController {

    private final ArticleLikeService articleLikeService;
    private final CommentLikeService commentLikeService;

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Article_Like)
    @PostMapping("/boards/{articleId}")
    public ResponseEntity<?> articleLikeSave(@PathVariable Integer memberId, @PathVariable Integer articleId) {
        Integer likeId = articleLikeService.saveArticleLike(articleId, memberId);

        return (likeId > 0) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Article_Like)
    @DeleteMapping("/boards/{articleId}")
    public ResponseEntity<?> articleLikeRemove(@PathVariable Integer memberId, @PathVariable Integer articleId) {
        Integer removeCnt = articleLikeService.removeArticleLike(articleId, memberId);

        return (removeCnt > 0) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Comment_Like)
    @PostMapping("comments/{commentId}")
    public ResponseEntity<?> commentLikeSave(@PathVariable Integer memberId, @PathVariable Integer commentId) {
        Integer likeId = commentLikeService.saveCommentLike(commentId, memberId);

        return (likeId > 0) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }

    @JWTRequired(required = true)
    @Authority(authorityType = AuthorityType.Member_Comment_Like)
    @DeleteMapping("comments/{commentId}")
    public ResponseEntity<?> commentLikeRemove(@PathVariable Integer memberId, @PathVariable Integer commentId) {
        Integer removeCnt = commentLikeService.removeCommentLike(commentId, memberId);

        return (removeCnt > 0) ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }
}
