package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.domain.board.StudyBoardComment;
import com.ssafy.interviewstudy.domain.notification.Notification;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.StudyBoardCommentResponse;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.repository.board.StudyBoardCommentRepository;
import com.ssafy.interviewstudy.repository.board.StudyBoardRepository;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudyBoardCommentService {

    private final StudyBoardCommentRepository commentRepository;
    private final StudyBoardRepository boardRepository;
    private final StudyBoardCommentDtoService commentDtoService;
    private final NotificationService notificationService;

    // 게시글 댓글 저장
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
                                .memberId(commentRequest.getMemberId())
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
    public Integer saveCommentReply(Integer articleId, Integer commentId, CommentRequest commentRequest){
        commentRequest.setArticleId(articleId);
        StudyBoardComment comment = commentDtoService.toEntityWithParent(commentId, commentRequest);
        //스터디 게시판 댓글에 대댓글이 달림
        notificationService.sendNotificationToMember(
                NotificationDto
                        .builder()
                        .memberId(commentRequest.getMemberId())
                        .content("스터디 게시판 댓글에 답글이 달렸습니다")
                        .notificationType(NotificationType.StudyReply)
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
    public StudyBoardCommentResponse modifyComment(Integer commentId, CommentRequest commentRequest){

        StudyBoardComment originComment = commentRepository.findById(commentId).get();

        originComment.modifyComment(commentRequest);
        StudyBoardComment modifiedComment = commentRepository.save(originComment);

        return commentDtoService.fromEntity(modifiedComment);
    }

    // (대)댓글 삭제
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
