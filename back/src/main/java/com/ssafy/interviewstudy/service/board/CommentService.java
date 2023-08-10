package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.CommentLike;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.notification.Notification;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.CommentResponse;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.repository.board.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.BoardRepository;
import com.ssafy.interviewstudy.repository.board.CommentLikeRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final BoardRepository boardRepository;
    private final ArticleCommentRepository articleCommentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final MemberRepository memberRepository;
    private final CommentDtoService commentDtoService;

    private final NotificationService notificationService;


    // 게시글 댓글 저장
    @Transactional
    public Integer saveComment(Integer articleId, CommentRequest commentRequest){
        commentRequest.setArticleId(articleId);
        ArticleComment comment = articleCommentRepository.save(commentDtoService.toEntity(commentRequest));

        //댓글이 달릴 게시글 작성자에게 알림을 보내줘야함
       if(comment.getId()!=null) {
           notificationService.sendNotificationToMember(
                   NotificationDto
                           .builder()
                           .memberId(commentRequest.getMemberId())
                           .content("게시글"+comment.getArticle().getTitle()+"에 댓글이 달렸습니다. ")
                           .notificationType(NotificationType.BoardComment)
                           .url(articleId.toString())
                           .isRead(false)
                           .build()
           );
       }
        return comment.getId();
    }

    // 대댓글 저장
    @Transactional
    public Integer saveCommentReply(Integer articleId, Integer commentId, CommentRequest commentRequest){
        commentRequest.setArticleId(articleId);
        ArticleComment comment = commentDtoService.toEntityWithParent(commentId, commentRequest);
        articleCommentRepository.save(comment);
        //대댓글이 달릴 댓글의 작성자에게 알림 보내기
        notificationService
                .sendNotificationToMember(
                        NotificationDto
                                .builder()
                                .memberId(commentRequest.getMemberId())
                                .content("댓글에 대댓글이 달렸습니다.")
                                .url(comment.getArticle().getId().toString()+" "+comment.getId().toString())
                                .notificationType(NotificationType.BoardReply)
                                .isRead(false)
                                .build()
                );

        return comment.getId();
    }

    // 게시글 댓글 조회
    public List<CommentResponse> findComments(Integer memberId, Integer articleId){
        Sort sort = Sort.by(
                Sort.Order.asc("createdAt"),
                Sort.Order.asc("cr.createdAt")
        );
        List<ArticleComment> comment = articleCommentRepository.findAllByArticle(boardRepository.findById(articleId).get());
        System.out.println(comment.size());
        findReplies(comment);
        List<CommentResponse> commentResponses = new ArrayList<>();

        for (ArticleComment c: comment) {
            commentResponses.add(commentDtoService.fromEntity(memberId, c));
        }

        return commentResponses;
    }

    public void findReplies(List<ArticleComment> parents){
        for (ArticleComment comment: parents) {
            List<ArticleComment> replies = comment.getReplies();
        }
    }

    // (대)댓글 수정
    @Transactional
    public CommentResponse modifyComment(Integer commentId, CommentRequest commentRequest){

        ArticleComment originComment = articleCommentRepository.findById(commentId).get();

        originComment.modifyComment(commentRequest);
        ArticleComment modifiedComment = articleCommentRepository.save(originComment);

        return commentDtoService.fromEntity(commentRequest.getMemberId(), modifiedComment);
    }

    // (대)댓글 삭제
    @Transactional
    public void removeComment(Integer commentId){
        ArticleComment comment = articleCommentRepository.findById(commentId).get();
        comment.deleteComment();
        articleCommentRepository.save(comment);
    }

    // 댓글 좋아요
    @Transactional
    public Integer saveCommentLike(Integer memberId, Integer commentId){
        Member member = memberRepository.findMemberById(memberId);
        ArticleComment comment = articleCommentRepository.findById(commentId).get();

        // 이미 좋아요를 누른 회원인지 체크
        if(checkMemberLikeComment(memberId, commentId))
            return 0;

        CommentLike commentLike =commentLikeRepository.save(CommentLike.builder()
                .member(member)
                .comment(comment)
                .build());
        return commentLike.getId();
    }

    // 댓글 좋아요 삭제
    @Transactional
    public void removeCommentLike(Integer memberId, Integer commentId){
        Member member = memberRepository.findMemberById(memberId);
        ArticleComment comment = articleCommentRepository.findById(commentId).get();

        if(checkMemberLikeComment(memberId, commentId))
            commentLikeRepository.removeCommentLikeByCommentAndMember(comment, member);
    }

    public Boolean checkMemberLikeComment(Integer memberId, Integer commentId){
        return commentLikeRepository.existsByMember_IdAndComment_Id(memberId, commentId);
    }

    // 댓글 작성자가 본인인지 체크
    public Boolean checkAuthor(Integer commentId, Integer memberId){
        ArticleComment comment = articleCommentRepository.findById(commentId).get();

        if(comment.getAuthor().getId() == memberId) return true;
        else return false;
    }
}
