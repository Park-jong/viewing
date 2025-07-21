package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.CommentLike;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.CommentResponse;
import com.ssafy.interviewstudy.exception.board.BoardExceptionFactory;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.board.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.BoardRepository;
import com.ssafy.interviewstudy.repository.board.CommentLikeRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BoardRepository boardRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentDtoManager commentDtoManager;
    private final NotificationService notificationService;

    // 게시글 댓글 저장
    @Transactional
    @Override
    public Integer saveComment(Integer articleId, CommentRequest commentRequest) {
        commentRequest.setArticleId(articleId);
        ArticleComment comment = articleCommentRepository.save(commentDtoManager.fromRequestToEntity(commentRequest));
        notificationService.sendNotificationAboutComment(comment, NotificationType.BoardComment);
        return comment.getId();
    }

    // 대댓글 저장
    @Transactional
    @Override
    public Integer saveCommentReply(Integer articleId, Integer commentId, CommentRequest commentRequest) {
        ArticleComment parentComment = articleCommentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound);
        commentRequest.setArticleId(articleId);
        ArticleComment reply = commentDtoManager.fromRequestToEntityWithParent(commentId, commentRequest);
        articleCommentRepository.save(reply);
        notificationService.sendNotificationAboutComment(reply, NotificationType.BoardComment);
        return reply.getId();
    }

    // 게시글 댓글 조회
    @Override
    public List<CommentResponse> findComments(Integer memberId, Integer articleId) {
        Sort sort = Sort.by(Sort.Order.asc("createdAt"), Sort.Order.asc("cr.createdAt"));
        Board board = boardRepository.findById(articleId).orElseThrow(BoardExceptionFactory::articleNotFound);
        List<ArticleComment> comment = articleCommentRepository.findAllByArticle(board);
        return comment.stream().map((c)->commentDtoManager.fromEntityToResponse(memberId, c)).collect(Collectors.toList());
    }

    // (대)댓글 수정
    @Transactional
    @Override
    public CommentResponse modifyComment(Integer commentId, CommentRequest commentRequest) {
        ArticleComment originComment = articleCommentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound);
        originComment.modifyComment(commentRequest);
        ArticleComment modifiedComment = articleCommentRepository.save(originComment);
        return commentDtoManager.fromEntityToResponse(commentRequest.getMemberId(), modifiedComment);
    }

    // (대)댓글 삭제
    @Transactional
    @Override
    public void removeComment(Integer commentId) {
        ArticleComment comment = articleCommentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound);
        comment.deleteComment();
        articleCommentRepository.save(comment);
    }

    // 댓글 좋아요
    @Transactional
    @Override
    public Integer saveCommentLike(Integer memberId, Integer commentId) {
        Member member = memberRepository.findMemberById(memberId);
        ArticleComment comment = articleCommentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound);
        // 이미 좋아요를 누른 회원인지 체크
        if (checkMemberLikeComment(memberId, commentId)) {
            return 0;
        }
        CommentLike commentLike = commentLikeRepository.save(CommentLike.builder().member(member).comment(comment).build());
        return commentLike.getId();
    }

    private Boolean checkMemberLikeComment(Integer memberId, Integer commentId) {
        return commentLikeRepository.existsByMember_IdAndComment_Id(memberId, commentId);
    }

    // 댓글 좋아요 삭제
    @Transactional
    @Override
    public void removeCommentLike(Integer memberId, Integer commentId) {
        Member member = memberRepository.findMemberById(memberId);
        ArticleComment comment = articleCommentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound);
        if (checkMemberLikeComment(memberId, commentId)) {
            commentLikeRepository.removeCommentLikeByCommentAndMember(comment, member);
        }
    }

    // 댓글 작성자가 본인인지 체크
    @Override
    public Boolean checkAuthor(Integer commentId, Integer memberId) {
        ArticleComment comment = articleCommentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound);
        return Objects.equals(comment.getAuthor().getId(), memberId);
    }
}
