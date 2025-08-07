package com.ssafy.interviewstudy.service.board.studyBoard;

import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.board.StudyBoardComment;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.*;
import com.ssafy.interviewstudy.exception.board.BoardExceptionFactory;
import com.ssafy.interviewstudy.repository.board.studyBoard.StudyBoardCommentRepository;
import com.ssafy.interviewstudy.repository.board.studyBoard.StudyBoardRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StudyBoardCommentDtoManager {

    private final MemberRepository memberRepository;
    private final StudyBoardRepository boardRepository;
    private final StudyBoardCommentRepository commentRepository;

    public StudyBoardComment toEntity(CommentRequest commentRequest) {
        Member author = memberRepository.findMemberById(commentRequest.getMemberId()).orElseThrow(BoardExceptionFactory::memberNotFound);
        StudyBoard article = boardRepository.findById(commentRequest.getArticleId()).orElseThrow(BoardExceptionFactory::articleNotFound);
        return StudyBoardComment.builder()
                .author(author)
                .article(article)
                .isDelete(false)
                .content(commentRequest.getContent()).build();
    }

    public StudyBoardComment toEntityWithParent(Integer commentId, CommentRequest commentRequest) {
        StudyBoardComment comment = toEntity(commentRequest);
        StudyBoardComment parent = commentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound);
        comment.setComment(parent);
        return comment;
    }

    public StudyBoardCommentResponse fromEntity(StudyBoardComment articleComment) {
        StudyBoardCommentResponse commentResponse = StudyBoardCommentResponse.builder()
                .commentId(articleComment.getId())
                .content(articleComment.getContent())
                .author(new Author(articleComment.getAuthor()))
                .isDelete(articleComment.getIsDelete())
                .createdAt(articleComment.getCreatedAt())
                .updatedAt(articleComment.getUpdatedAt())
                .build();
        commentResponse.setReplies(fromEntity(articleComment.getReplies()));
        commentResponse.setCommentCount(articleComment.getReplies().size());
        return commentResponse;
    }

    public List<StudyBoardCommentReplyResponse> fromEntity(List<StudyBoardComment> replies) {
        List<StudyBoardCommentReplyResponse> replyResponses = new ArrayList<>();
        for (StudyBoardComment c : replies) {
            replyResponses.add(StudyBoardCommentReplyResponse.builder()
                    .commentId(c.getId())
                    .content(c.getContent())
                    .author(new Author(c.getAuthor()))
                    .isDelete(c.getIsDelete())
                    .createdAt(c.getCreatedAt())
                    .updatedAt(c.getUpdatedAt())
                    .build());
        }
        return replyResponses;
    }

}
