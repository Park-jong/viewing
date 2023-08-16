package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.domain.board.StudyBoardComment;
import com.ssafy.interviewstudy.domain.notification.Notification;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.StudyBoardCommentResponse;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.board.StudyBoardCommentRepository;
import com.ssafy.interviewstudy.repository.board.StudyBoardRepository;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyBoardCommentService {

    private final StudyBoardCommentRepository commentRepository;
    private final StudyBoardRepository boardRepository;
    private final StudyBoardCommentDtoService commentDtoService;
    private final NotificationService notificationService;

    // 게시글 댓글 저장
    @Transactional
    @JWTRequired
    public Integer saveComment(Integer articleId, CommentRequest commentRequest){
        commentRequest.setArticleId(articleId);
        StudyBoardComment comment = commentRepository.save(commentDtoService.toEntity(commentRequest));

        //스터디 게시글에 댓글이 달릴 경우 알림을 보내줘야함
        if(comment.getId()!=null){
            if(comment.getId()!=null) {
                notificationService.sendNotificationToMember(
                        NotificationDto
                                .builder()
                                .memberId(comment.getArticle().getAuthor().getId())
                                .content("스터디 게시판 게시글"+comment.getArticle().getTitle()+"에 댓글이 달렸습니다. ")
                                .notificationType(NotificationType.StudyComment)
                                .url(comment.getArticle().getStudy().getId().toString()+" "+articleId.toString())
                                .build()
                );
            }
        }
        return comment.getId();
    }

    // 대댓글 저장
    @Transactional
    public Integer saveCommentReply(Integer articleId, Integer commentId, CommentRequest commentRequest){
        commentRequest.setArticleId(articleId);
        StudyBoardComment comment = commentDtoService.toEntityWithParent(commentId, commentRequest);
        Optional<StudyBoardComment> parentComment = commentRepository.findById(commentId);
        if(parentComment.isEmpty()){
            throw new NotFoundException("대댓글 대상인 댓글이 존재하지 않습니다.");
        }
        //스터디 게시판 댓글에 대댓글이 달림
        notificationService.sendNotificationToMember(
                NotificationDto
                        .builder()
                        .memberId(parentComment.get().getAuthor().getId())
                        .content("스터디 게시판 댓글에 답글이 달렸습니다")
                        .notificationType(NotificationType.StudyReply)
                        .isRead(false)
                        .url(comment.getArticle().getStudy().getId().toString()+" "+articleId.toString())
                        .build()
        );
        return commentRepository.save(comment).getId();
    }

    // 게시글 댓글 조회
    public List<StudyBoardCommentResponse> findComments(Integer articleId){
        List<StudyBoardComment> comment = commentRepository.findAllByArticle(boardRepository.findById(articleId).get());
        System.out.println(comment.size());
        findReplies(comment);
        List<StudyBoardCommentResponse> commentResponses = new ArrayList<>();

        for (StudyBoardComment c: comment) {
            commentResponses.add(commentDtoService.fromEntity(c));
        }

        return commentResponses;
    }

    public void findReplies(List<StudyBoardComment> parents){
        for (StudyBoardComment comment: parents) {
            List<StudyBoardComment> replies = comment.getReplies();
        }
    }

    // (대)댓글 수정
    @Transactional
    public StudyBoardCommentResponse modifyComment(Integer commentId, CommentRequest commentRequest){

        StudyBoardComment originComment = commentRepository.findById(commentId).get();

        originComment.modifyComment(commentRequest);
        StudyBoardComment modifiedComment = commentRepository.save(originComment);

        return commentDtoService.fromEntity(modifiedComment);
    }

    // (대)댓글 삭제
    @Transactional
    public void removeComment(Integer commentId){
        StudyBoardComment comment = commentRepository.findById(commentId).get();
        comment.deleteComment();
        commentRepository.save(comment);
    }


    // 댓글 작성자가 본인인지 체크
    public Boolean checkAuthor(Integer commentId, Integer memberId){
        StudyBoardComment comment = commentRepository.findById(commentId).get();

        if(comment.getAuthor().getId() == memberId) return true;
        else return false;
    }
}
