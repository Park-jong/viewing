package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.board.CommentReplyResponse;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.CommentResponse;
import com.ssafy.interviewstudy.repository.board.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.BoardRepository;
import com.ssafy.interviewstudy.repository.board.CommentLikeRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.service.redis.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentDtoService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final ArticleCommentRepository commentRepository;
    private final CommentLikeService commentLikeService;

    public ArticleComment toEntity(CommentRequest commentRequest){
        Member author = memberRepository.findMemberById(commentRequest.getMemberId());

        ArticleComment articleComment = ArticleComment.builder()
                .author(author)
                .article(boardRepository.findById(commentRequest.getArticleId()).get())
                .isDelete(false)
                .content(commentRequest.getContent()).build();

        return articleComment;
    }

    public ArticleComment toEntityWithParent(Integer commentId, CommentRequest commentRequest){
        ArticleComment comment = toEntity(commentRequest);
        comment.setComment(commentRepository.findById(commentId).get());

        return comment;
    }

    public CommentResponse fromEntity(Integer memberId, ArticleComment articleComment){
        CommentResponse commentResponse = CommentResponse.builder()
                .commentId(articleComment.getId())
                .content(articleComment.getContent())
                .author(new Author(articleComment.getAuthor()))
                .isDelete(articleComment.getIsDelete())
                .createdAt(articleComment.getCreatedAt())
                .updatedAt(articleComment.getUpdatedAt())
                .likeCount(commentLikeService.getLikeCount(articleComment.getId()))
                .build();

        if(memberId != null)
            commentResponse.setIsLike(commentLikeService.checkMemberLikeComment(articleComment.getId(), memberId));

        commentResponse.setReplies(fromEntity(memberId, articleComment.getReplies()));
        commentResponse.setCommentCount(articleComment.getReplies().size());

        return commentResponse;
    }

    public List<CommentReplyResponse> fromEntity(Integer memberId, List<ArticleComment> replies){
        List<CommentReplyResponse> replyResponses = new ArrayList<>();
        for (ArticleComment c: replies) {
            if(memberId != null){
                replyResponses.add(CommentReplyResponse.builder()
                        .commentId(c.getId())
                        .content(c.getContent())
                        .author(new Author(c.getAuthor()))
                        .isDelete(c.getIsDelete())
                        .createdAt(c.getCreatedAt())
                        .updatedAt(c.getUpdatedAt())
                        .likeCount(commentLikeService.getLikeCount(c.getId()))
                        .isLike(commentLikeService.checkMemberLikeComment(c.getId(), memberId))
                        .build());
            }else{
                replyResponses.add(CommentReplyResponse.builder()
                        .commentId(c.getId())
                        .content(c.getContent())
                        .author(new Author(c.getAuthor()))
                        .isDelete(c.getIsDelete())
                        .createdAt(c.getCreatedAt())
                        .updatedAt(c.getUpdatedAt())
                        .likeCount(commentLikeService.getLikeCount(c.getId()))
                        .build());
            }

        }
        return replyResponses;
    }

}
