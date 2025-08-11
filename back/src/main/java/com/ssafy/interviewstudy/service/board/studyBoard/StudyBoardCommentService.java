package com.ssafy.interviewstudy.service.board.studyBoard;

import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.domain.board.StudyBoardComment;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.StudyBoardCommentResponse;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface StudyBoardCommentService {
    // 게시글 댓글 저장
    Integer saveComment(Integer articleId, CommentRequest commentRequest);

    // 대댓글 저장
    Integer saveCommentReply(Integer articleId, Integer commentId, CommentRequest commentRequest);

    // 게시글 댓글 조회
    List<StudyBoardCommentResponse> findComments(Integer articleId);

    void findReplies(List<StudyBoardComment> parents);

    // (대)댓글 수정
    StudyBoardCommentResponse modifyComment(Integer commentId, CommentRequest commentRequest);

    // (대)댓글 삭제
    void removeComment(Integer commentId);

    // 댓글 작성자가 본인인지 체크
    Boolean checkAuthor(Integer commentId, Integer memberId);
}
