package com.ssafy.interviewstudy.service.board.studyBoard;

import com.ssafy.interviewstudy.domain.board.*;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.StudyBoardCommentResponse;
import com.ssafy.interviewstudy.repository.board.studyBoard.StudyBoardCommentRepository;
import com.ssafy.interviewstudy.repository.board.studyBoard.StudyBoardRepository;
import com.ssafy.interviewstudy.service.notification.NotificationDtoManager;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class StudyBoardCommentServiceTest {
    @Mock
    private StudyBoardCommentRepository commentRepository;
    @Mock
    private StudyBoardRepository boardRepository;
    @Mock
    private StudyBoardCommentDtoManager commentDtoManager;
    @Mock
    private NotificationService notificationService;
    @Mock
    private NotificationDtoManager notificationDtoManager;
    @InjectMocks
    private StudyBoardCommentServiceImpl studyBoardCommentService;

    CommentRequest mockCommentRequest;
    CommentRequest mockReplyRequest;
    CommentRequest mockUpdateRequest;
    StudyBoardComment mockArticleComment;
    StudyBoardComment mockReply;
    StudyBoardComment mockUpdate;
    StudyBoardCommentResponse mockCommentResponse;
    StudyBoardCommentResponse mockReplyResponse;
    StudyBoardCommentResponse mockUpdateResponse;
    StudyBoard mockArticle;
    Member mockMember;
    Study mockStudy;
    List<StudyBoardComment> mockCommentList = new ArrayList<>();
    int studyId = 5;
    int articleId = 2;
    int memberId = 3;
    int commentId = 1;
    int replyId = 4;
    String memberName = "홍길동";
    String title = "기본 제목";
    String content = "기본 내용";
    String comment = "댓글 내용";
    String m_comment = "댓글 수정";
    String reply = "대댓글 내용";

    @BeforeEach
    void setUp() {
        mockMember = createMockMember();
        mockStudy = createMockStudy();
        mockArticle = createMockBoard(mockMember);
        mockArticleComment = createMockComment();
        mockCommentRequest = createMockCommentRequest();
        mockReply = createMockReply();
        mockReplyRequest = createMockReplyRequest();
        mockCommentResponse = createMockCommentResponse();
        mockReplyResponse = createMockReplyResponse();
        mockUpdate = createUpdateMockComment();
        mockUpdateRequest = createUpdateMockCommentRequest();
        mockUpdateResponse = createUpdateMockCommentResponse();
        mockCommentList.add(mockArticleComment);
        mockCommentList.add(mockReply);
    }

    private Member createMockMember() {
        return Member.builder()
                .id(memberId)
                .nickname(memberName)
                .build();
    }

    private Study createMockStudy() {
        return Study.builder()
                .id(studyId)
                .title("스터디명")
                .leader(mockMember)
                .build();
    }

    private StudyBoard createMockBoard(Member author) {
        return StudyBoard.builder()
                .study(mockStudy)
                .id(articleId)
                .title(title)
                .content(content)
                .author(author)
                .build();
    }

    private StudyBoardComment createMockComment() {
        StudyBoardComment studyComment = new StudyBoardComment();
        studyComment.setId(commentId);
        studyComment.setArticle(mockArticle);
        studyComment.setAuthor(mockMember);
        studyComment.setContent(comment);
        return studyComment;
    }

    private CommentRequest createMockCommentRequest() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setArticleId(articleId);
        commentRequest.setMemberId(memberId);
        commentRequest.setContent(comment);
        return commentRequest;
    }

    private StudyBoardCommentResponse createMockCommentResponse() {
        return StudyBoardCommentResponse.builder()
                .content(comment)
                .build();
    }

    private StudyBoardComment createUpdateMockComment() {
        StudyBoardComment comment = new StudyBoardComment();
        comment.setId(commentId);
        comment.setArticle(mockArticle);
        comment.setAuthor(mockMember);
        comment.setContent(m_comment);
        return comment;
    }

    private CommentRequest createUpdateMockCommentRequest() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setArticleId(articleId);
        commentRequest.setMemberId(memberId);
        commentRequest.setContent(m_comment);
        return commentRequest;
    }

    private StudyBoardCommentResponse createUpdateMockCommentResponse() {
        return StudyBoardCommentResponse.builder()
                .content(m_comment)
                .build();

    }

    private StudyBoardComment createMockReply() {
        StudyBoardComment mockReply = new StudyBoardComment();
        mockReply.setId(replyId);
        mockReply.setArticle(mockArticle);
        mockReply.setAuthor(mockMember);
        mockReply.setContent(reply);
        mockReply.setComment(mockArticleComment);
        return mockReply;
    }

    private CommentRequest createMockReplyRequest() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setArticleId(articleId);
        commentRequest.setMemberId(memberId);
        commentRequest.setContent(reply);
        return commentRequest;
    }

    private StudyBoardCommentResponse createMockReplyResponse() {
        return StudyBoardCommentResponse.builder()
                .content(reply)
                .build();
    }

    @Test
    void saveComment() {
        //given
        Mockito.when(commentRepository.save(any(StudyBoardComment.class))).thenReturn(mockArticleComment);
        Mockito.when(commentDtoManager.toEntity(mockCommentRequest)).thenReturn(mockArticleComment);
        //when
        int result = studyBoardCommentService.saveComment(articleId, mockCommentRequest);
        //then
        assertThat(result).isEqualTo(commentId);
    }

    @Test
    void saveCommentReply() {
        //given
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(mockArticleComment));
        Mockito.when(commentRepository.save(any(StudyBoardComment.class))).thenReturn(mockReply);
        Mockito.when(commentDtoManager.toEntityWithParent(commentId, mockReplyRequest)).thenReturn(mockReply);
        //when
        int result = studyBoardCommentService.saveCommentReply(articleId, commentId, mockReplyRequest);
        //then
        assertThat(result).isEqualTo(replyId);
    }

    @Test
    void findComments() {
        //given
        Mockito.when(boardRepository.findById(articleId)).thenReturn(Optional.ofNullable(mockArticle));
        Mockito.when(commentRepository.findAllByArticle(mockArticle)).thenReturn(mockCommentList);
        Mockito.when(commentDtoManager.fromEntity(mockArticleComment)).thenReturn(mockCommentResponse);
        Mockito.when(commentDtoManager.fromEntity(mockReply)).thenReturn(mockReplyResponse);
        //when
        List<StudyBoardCommentResponse> result = studyBoardCommentService.findComments(articleId);
        //then
        assertThat(result).hasSize(2);
    }

    @Test
    void modifyComment() {
        //given
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(mockArticleComment));
        Mockito.when(commentRepository.save(any(StudyBoardComment.class))).thenReturn(mockArticleComment);
        Mockito.when(commentDtoManager.fromEntity(any(StudyBoardComment.class))).thenReturn(mockUpdateResponse);
        //when
        StudyBoardCommentResponse result = studyBoardCommentService.modifyComment(commentId, mockUpdateRequest);
        //then
        assertThat(result).isEqualTo(mockUpdateResponse);
        assertThat(result).isNotEqualTo(mockCommentResponse);
    }

    @Test
    void removeComment() {
        //given
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.ofNullable(mockArticleComment));
        //when
        studyBoardCommentService.removeComment(commentId);
        //then
        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, times(1)).save(mockArticleComment);
    }
}