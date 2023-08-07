package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.StudyBoardComment;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.*;
import com.ssafy.interviewstudy.repository.board.*;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StudyBoardCommentDtoService {

    private final MemberRepository memberRepository;
    private final StudyBoardRepository boardRepository;
    private final StudyBoardCommentRepository commentRepository;

    public StudyBoardComment toEntity(CommentRequest commentRequest){
        Member author = memberRepository.findMemberById(commentRequest.getMemberId());

        StudyBoardComment articleComment = StudyBoardComment.builder()
                .author(author)
                .article(boardRepository.findById(commentRequest.getArticleId()).get())
                .isDelete(false)
                .content(commentRequest.getContent()).build();

        return articleComment;
    }

    public StudyBoardComment toEntityWithParent(Integer commentId, CommentRequest commentRequest){
        StudyBoardComment comment = toEntity(commentRequest);
        comment.setComment(commentRepository.findById(commentId).get());

        return comment;
    }

    public StudyBoardCommentResponse fromEntity(StudyBoardComment articleComment){
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

    public List<StudyBoardCommentReplyResponse> fromEntity(List<StudyBoardComment> replies){
        List<StudyBoardCommentReplyResponse> replyResponses = new ArrayList<>();
        for (StudyBoardComment c: replies) {
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
