package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.board.CommentLike;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.CommentResponse;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final BoardRepository boardRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentDtoService commentDtoService;
    private final NotificationService notificationService;


    // 게시글 댓글 저장
    @Transactional
    @Override
    public Integer saveComment(Integer articleId, CommentRequest commentRequest) {
        commentRequest.setArticleId(articleId);
        ArticleComment comment = articleCommentRepository.save(commentDtoService.toEntity(commentRequest));
        //댓글이 달릴 게시글 작성자에게 알림
        if (comment.getId() != null) {
            sendNotification(comment, NotificationType.BoardComment);
        }
        return comment.getId();
    }

    private void sendNotification(ArticleComment comment, NotificationType type) {
        Integer receiver = comment.getArticle().getAuthor().getId();
        String content;
        if (type == NotificationType.BoardComment) {
            content = newCommentNotification(comment);
        } else if (type == NotificationType.BoardReply) {
            content = newReplyNotification(comment);
        } else {
            content = "";
        }
        String url = getUrl(comment);
        NotificationDto notification = makeNotification(receiver, content, type, url, false);
        notificationService.sendNotificationToMember(notification);
    }

    private String newCommentNotification(ArticleComment comment) {
        return "게시글" + comment.getArticle().getTitle() + "에 댓글이 달렸습니다.";
    }

    private String getUrl(ArticleComment comment) {
        return boardTypeToUrl(comment.getArticle().getBoardType()) + " " + comment.getArticle().getId().toString();
    }

    private NotificationDto makeNotification(Integer receiver, String content, NotificationType type, String url, boolean isRead) {
        return NotificationDto
                .builder()
                .memberId(receiver)
                .content(content)
                .notificationType(type)
                .url(url)
                .isRead(isRead)
                .build();
    }

    // 대댓글 저장
    @Transactional
    @Override
    public Integer saveCommentReply(Integer articleId, Integer commentId, CommentRequest commentRequest) {
        commentRequest.setArticleId(articleId);
        ArticleComment comment = commentDtoService.toEntityWithParent(commentId, commentRequest);
        Optional<ArticleComment> parentComment = articleCommentRepository.findById(commentId);
        if (parentComment.isEmpty()) {
            throw new NotFoundException("대댓글 대상인 댓글이 없습니다.");
        }
        articleCommentRepository.save(comment);
        sendNotification(comment, NotificationType.BoardComment);
        return comment.getId();
    }

    private String newReplyNotification(ArticleComment comment) {
        return "댓글에 대댓글이 달렸습니다.";
    }

    // 게시글 댓글 조회
    @Override
    public List<CommentResponse> findComments(Integer memberId, Integer articleId) {
        Sort sort = Sort.by(
                Sort.Order.asc("createdAt"),
                Sort.Order.asc("cr.createdAt")
        );
        List<ArticleComment> comment = articleCommentRepository.findAllByArticle(boardRepository.findById(articleId).get());
        System.out.println(comment.size());
        findReplies(comment);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (ArticleComment c : comment) {
            commentResponses.add(commentDtoService.fromEntity(memberId, c));
        }
        return commentResponses;
    }

    private void findReplies(List<ArticleComment> parents) {
        for (ArticleComment comment : parents) {
            List<ArticleComment> replies = comment.getReplies();
        }
    }

    // (대)댓글 수정
    @Transactional
    @Override
    public CommentResponse modifyComment(Integer commentId, CommentRequest commentRequest) {
        ArticleComment originComment = articleCommentRepository.findById(commentId).get();
        originComment.modifyComment(commentRequest);
        ArticleComment modifiedComment = articleCommentRepository.save(originComment);
        return commentDtoService.fromEntity(commentRequest.getMemberId(), modifiedComment);
    }

    // (대)댓글 삭제
    @Transactional
    @Override
    public void removeComment(Integer commentId) {
        ArticleComment comment = articleCommentRepository.findById(commentId).get();
        comment.deleteComment();
        articleCommentRepository.save(comment);
    }

    // 댓글 좋아요
    @Transactional
    @Override
    public Integer saveCommentLike(Integer memberId, Integer commentId) {
        Member member = memberRepository.findMemberById(memberId);
        ArticleComment comment = articleCommentRepository.findById(commentId).get();
        // 이미 좋아요를 누른 회원인지 체크
        if (checkMemberLikeComment(memberId, commentId)) {
            return 0;
        }
        CommentLike commentLike = commentLikeRepository.save(CommentLike.builder()
                .member(member)
                .comment(comment)
                .build());
        return commentLike.getId();
    }

    // 댓글 좋아요 삭제
    @Transactional
    @Override
    public void removeCommentLike(Integer memberId, Integer commentId) {
        Member member = memberRepository.findMemberById(memberId);
        ArticleComment comment = articleCommentRepository.findById(commentId).get();
        if (checkMemberLikeComment(memberId, commentId)) {
            commentLikeRepository.removeCommentLikeByCommentAndMember(comment, member);
        }
    }

    private Boolean checkMemberLikeComment(Integer memberId, Integer commentId) {
        return commentLikeRepository.existsByMember_IdAndComment_Id(memberId, commentId);
    }

    // 댓글 작성자가 본인인지 체크
    @Override
    public Boolean checkAuthor(Integer commentId, Integer memberId) {
        ArticleComment comment = articleCommentRepository.findById(commentId).get();
        return comment.getAuthor().getId() == memberId;
    }

    private static String boardTypeToUrl(BoardType boardType) {
        if (boardType == BoardType.review) {
            return "interview";
        }
        if (boardType == BoardType.qna) {
            return "question";
        }
        if (boardType == BoardType.general) {
            return "free";
        }
        return "free";
    }
}
