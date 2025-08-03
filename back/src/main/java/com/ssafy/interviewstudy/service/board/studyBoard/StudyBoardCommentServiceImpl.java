package com.ssafy.interviewstudy.service.board.studyBoard;

import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.board.StudyBoardComment;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.StudyBoardCommentResponse;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.exception.board.BoardExceptionFactory;
import com.ssafy.interviewstudy.repository.board.studyBoard.StudyBoardCommentRepository;
import com.ssafy.interviewstudy.repository.board.studyBoard.StudyBoardRepository;
import com.ssafy.interviewstudy.service.notification.NotificationDtoManager;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StudyBoardCommentServiceImpl implements StudyBoardCommentService {

    private final StudyBoardCommentRepository commentRepository;
    private final StudyBoardRepository boardRepository;
    private final StudyBoardCommentDtoManager commentDtoManager;
    private final NotificationService notificationService;
    private final NotificationDtoManager notificationDtoManager;

    // 게시글 댓글 저장
    @Transactional
    @JWTRequired
    @Override
    public Integer saveComment(Integer articleId, CommentRequest commentRequest) {
        commentRequest.setArticleId(articleId);
        StudyBoardComment comment = commentRepository.save(commentDtoManager.toEntity(commentRequest));
        sendNotificationAboutComment(articleId, comment);
        return comment.getId();
    }

    private void sendNotificationAboutComment(Integer articleId, StudyBoardComment comment) {
        int receiver = comment.getArticle().getAuthor().getId();
        String content = String.format("스터디 게시판 게시글%s에 댓글이 달렸습니다. ", comment.getArticle().getTitle());
        NotificationType type = NotificationType.StudyComment;
        String url = String.format("%s %s", comment.getArticle().getStudy().getId().toString(), articleId.toString());
        boolean isRead = false;
        NotificationDto notificationDto = notificationDtoManager.makeNotification(receiver, content, type, url, isRead);
        notificationService.sendNotificationToMember(notificationDto);
    }

    // 대댓글 저장
    @Transactional
    @Override
    public Integer saveCommentReply(Integer articleId, Integer commentId, CommentRequest commentRequest) {
        commentRequest.setArticleId(articleId);
        StudyBoardComment comment = commentDtoManager.toEntityWithParent(commentId, commentRequest);
        StudyBoardComment parentComment = commentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound);
        sendNotificationAboutReply(articleId, parentComment);
        return commentRepository.save(comment).getId();
    }

    private void sendNotificationAboutReply(Integer articleId, StudyBoardComment parentComment) {
        int receiver = parentComment.getArticle().getAuthor().getId();
        String content = "스터디 게시판 댓글에 답글이 달렸습니다";
        NotificationType type = NotificationType.StudyReply;
        String url = String.format("%s %s", parentComment.getArticle().getStudy().getId().toString(), articleId.toString());
        boolean isRead = false;
        NotificationDto notificationDto = notificationDtoManager.makeNotification(receiver, content, type, url, isRead);
        notificationService.sendNotificationToMember(notificationDto);
    }


    // 게시글 댓글 조회
    @Override
    public List<StudyBoardCommentResponse> findComments(Integer articleId) {
        StudyBoard article = boardRepository.findById(articleId).orElseThrow(BoardExceptionFactory::articleNotFound);
        List<StudyBoardComment> comment = commentRepository.findAllByArticle(article);
        findReplies(comment);
        return comment.stream().map(commentDtoManager::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void findReplies(List<StudyBoardComment> parents) {
        for (StudyBoardComment comment : parents) {
            List<StudyBoardComment> replies = comment.getReplies();
        }
    }

    // (대)댓글 수정
    @Transactional
    @Override
    public StudyBoardCommentResponse modifyComment(Integer commentId, CommentRequest commentRequest) {
        StudyBoardComment originComment = commentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound);
        originComment.modifyComment(commentRequest);
        StudyBoardComment modifiedComment = commentRepository.save(originComment);
        return commentDtoManager.fromEntity(modifiedComment);
    }

    // (대)댓글 삭제
    @Transactional
    @Override
    public void removeComment(Integer commentId) {
        StudyBoardComment comment = commentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound);
        comment.deleteComment();
        commentRepository.save(comment);
    }


    // 댓글 작성자가 본인인지 체크
    @Override
    public Boolean checkAuthor(Integer commentId, Integer memberId) {
        StudyBoardComment comment = commentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound);
        if (comment.getAuthor().getId().equals(memberId)) {
            return true;
        }
        else return false;
    }
}
