package com.ssafy.interviewstudy.service.board.studyBoard;

import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.StudyBoardCommentResponse;

import java.util.List;

public interface StudyBoardCommentService {
    Integer saveComment(Integer articleId, CommentRequest commentRequest);

    Integer saveCommentReply(Integer articleId, Integer commentId, CommentRequest commentRequest);

    List<StudyBoardCommentResponse> findComments(Integer articleId);

    StudyBoardCommentResponse modifyComment(Integer commentId, CommentRequest commentRequest);

    void removeComment(Integer commentId);

    Boolean checkAuthor(Integer commentId, Integer memberId);
}
