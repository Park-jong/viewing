package com.ssafy.interviewstudy.service.board.generalBoard;

import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.CommentResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CommentService {
    // 게시글 댓글 저장
    @Transactional
    Integer saveComment(Integer articleId, CommentRequest commentRequest);

    // 대댓글 저장
    @Transactional
    Integer saveCommentReply(Integer articleId, Integer commentId, CommentRequest commentRequest);

    // 게시글 댓글 조회
    List<CommentResponse> findComments(Integer memberId, Integer articleId);

    // (대)댓글 수정
    @Transactional
    CommentResponse modifyComment(Integer commentId, CommentRequest commentRequest);

    // (대)댓글 삭제
    @Transactional
    void removeComment(Integer commentId);

    // 댓글 좋아요
    @Transactional
    Integer saveCommentLike(Integer memberId, Integer commentId);

    // 댓글 좋아요 삭제
    @Transactional
    void removeCommentLike(Integer memberId, Integer commentId);

    // 댓글 작성자가 본인인지 체크
    Boolean checkAuthor(Integer commentId, Integer memberId);
}
