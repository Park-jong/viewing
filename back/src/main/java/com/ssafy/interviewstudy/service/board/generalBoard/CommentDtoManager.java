package com.ssafy.interviewstudy.service.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.board.CommentReplyResponse;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.CommentResponse;
import com.ssafy.interviewstudy.exception.board.BoardExceptionFactory;
import com.ssafy.interviewstudy.repository.board.generalBoard.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.generalBoard.BoardRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.service.redis.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentDtoManager {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final ArticleCommentRepository commentRepository;
    private final CommentLikeService commentLikeService;

    public ArticleComment fromRequestToEntity(CommentRequest commentRequest){
        Member author = memberRepository.findMemberById(commentRequest.getMemberId()).orElseThrow(BoardExceptionFactory::memberNotFound);
        Board article = boardRepository.findById(commentRequest.getArticleId()).orElseThrow(BoardExceptionFactory::articleNotFound);
        return ArticleComment.builder()
                .author(author)
                .article(article)
                .isDelete(false)
                .content(commentRequest.getContent()).build();
    }

    public ArticleComment fromRequestToEntityWithParent(Integer commentId, CommentRequest commentRequest){
        ArticleComment comment = fromRequestToEntity(commentRequest);
        comment.setComment(commentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound));
        return comment;
    }

    // 댓글 목록 조회 전용: 좋아요 정보를 일괄 조회한 뒤 응답 생성
    public List<CommentResponse> fromEntitiesToResponses(Integer memberId, List<ArticleComment> comments) {
        // 댓글 + 대댓글 ID 전체 수집
        List<Integer> allIds = new ArrayList<>();
        comments.forEach(c -> {
            allIds.add(c.getId());
            c.getReplies().forEach(r -> allIds.add(r.getId()));
        });

        // 파이프라인으로 일괄 조회
        Map<Integer, Integer> likeCounts = commentLikeService.getLikeCountBatch(allIds);
        Set<Integer> likedIds = commentLikeService.getLikedCommentIdSet(allIds, memberId);

        return comments.stream()
                .map(c -> buildCommentResponse(memberId, c, likeCounts, likedIds))
                .collect(Collectors.toList());
    }

    private CommentResponse buildCommentResponse(Integer memberId, ArticleComment comment,
                                                  Map<Integer, Integer> likeCounts, Set<Integer> likedIds) {
        CommentResponse response = CommentResponse.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .author(new Author(comment.getAuthor()))
                .isDelete(comment.getIsDelete())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .likeCount(likeCounts.getOrDefault(comment.getId(), 0))
                .build();
        if (memberId != null) {
            response.setIsLike(likedIds.contains(comment.getId()));
        }
        response.setReplies(buildReplyResponses(memberId, comment.getReplies(), likeCounts, likedIds));
        response.setCommentCount(comment.getReplies().size());
        return response;
    }

    private List<CommentReplyResponse> buildReplyResponses(Integer memberId, List<ArticleComment> replies,
                                                            Map<Integer, Integer> likeCounts, Set<Integer> likedIds) {
        List<CommentReplyResponse> result = new ArrayList<>();
        for (ArticleComment c : replies) {
            CommentReplyResponse.CommentReplyResponseBuilder builder = CommentReplyResponse.builder()
                    .commentId(c.getId())
                    .content(c.getContent())
                    .author(new Author(c.getAuthor()))
                    .isDelete(c.getIsDelete())
                    .createdAt(c.getCreatedAt())
                    .updatedAt(c.getUpdatedAt())
                    .likeCount(likeCounts.getOrDefault(c.getId(), 0));
            if (memberId != null) {
                builder.isLike(likedIds.contains(c.getId()));
            }
            result.add(builder.build());
        }
        return result;
    }

    // 단건 조회용 (댓글 수정 응답 등)
    public CommentResponse fromEntityToResponse(Integer memberId, ArticleComment articleComment) {
        CommentResponse commentResponse = CommentResponse.builder()
                .commentId(articleComment.getId())
                .content(articleComment.getContent())
                .author(new Author(articleComment.getAuthor()))
                .isDelete(articleComment.getIsDelete())
                .createdAt(articleComment.getCreatedAt())
                .updatedAt(articleComment.getUpdatedAt())
                .likeCount(commentLikeService.getLikeCount(articleComment.getId()))
                .build();
        if (memberId != null)
            commentResponse.setIsLike(commentLikeService.checkMemberLikeComment(articleComment.getId(), memberId));
        commentResponse.setReplies(Collections.emptyList());
        commentResponse.setCommentCount(0);
        return commentResponse;
    }
}
