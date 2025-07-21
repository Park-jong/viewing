package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.CommentResponse;
import com.ssafy.interviewstudy.repository.board.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.BoardRepository;
import com.ssafy.interviewstudy.repository.board.CommentLikeRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ArticleCommentRepository articleCommentRepository;
    @Mock
    private CommentLikeRepository commentLikeRepository;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private CommentDtoManager commentDtoManager;
    @Mock
    private NotificationService notificationService;
    @Mock
    private NotificationDtoManager notificationDtoManager;
    @InjectMocks
    private CommentServiceImpl commentService;

    CommentRequest mockCommentRequest;
    CommentRequest mockReplyRequest;
    ArticleComment mockArticleComment;
    ArticleComment mockReply;
    CommentResponse mockCommentResponse;
    CommentResponse mockReplyResponse;
    Board mockArticle;
    Member mockMember;
    BoardType mockBoardType;
    List<ArticleComment> mockCommentList = new ArrayList<>();
    final int articleId = 2;
    final int memberId = 3;
    final int commentId = 1;
    final int replyId = 4;

    @BeforeEach
    void setUp() {
        mockMember = createMockMember();
        mockArticle = createMockBoard(mockMember);
        mockBoardType = BoardType.general;
        mockArticleComment = createMockComment();
        mockCommentRequest = createMockCommentRequest();
        mockReply = createMockReply();
        mockReplyRequest = createMockReplyRequest();
        mockCommentResponse = createMockCommentResponse();
        mockReplyResponse = createMockReplyResponse();
        mockCommentList.add(mockArticleComment);
        mockCommentList.add(mockReply);
    }

    private Member createMockMember() {
        return Member.builder()
                .id(memberId)
                .nickname("홍길동")
                .build();
    }

    private Board createMockBoard(Member author) {
        Board board = new Board();
        board.setId(articleId);
        board.setTitle("제목 관련");
        board.setContent("내용 관련");
        board.setAuthor(author);
        board.setBoardType(BoardType.general);
        board.setViewCount(0);
        return board;
    }

    private ArticleComment createMockComment() {
        ArticleComment comment = new ArticleComment();
        comment.setId(commentId);
        comment.setArticle(mockArticle);
        comment.setAuthor(mockMember);
        comment.setContent("댓글 내용");
        return comment;
    }

    private CommentRequest createMockCommentRequest() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setArticleId(articleId);
        commentRequest.setMemberId(memberId);
        commentRequest.setContent("댓글 내용");
        return commentRequest;
    }

    private CommentResponse createMockCommentResponse() {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setContent("댓글 내용");
        return commentResponse;
    }

    private ArticleComment createMockReply() {
        ArticleComment reply = new ArticleComment();
        reply.setId(replyId);
        reply.setArticle(mockArticle);
        reply.setAuthor(mockMember);
        reply.setContent("대댓글 내용");
        reply.setComment(mockArticleComment);
        return reply;
    }

    private CommentRequest createMockReplyRequest() {
        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setArticleId(articleId);
        commentRequest.setMemberId(memberId);
        commentRequest.setContent("대댓글 내용");
        return commentRequest;
    }

    private CommentResponse createMockReplyResponse() {
        CommentResponse commentResponse = new CommentResponse();
        commentResponse.setContent("대댓글 내용");
        return commentResponse;
    }

    @Test
    void saveComment() {
        //given
        Mockito.when(articleCommentRepository.save(any(ArticleComment.class))).thenReturn(mockArticleComment);
        Mockito.when(commentDtoManager.fromRequestToEntity(mockCommentRequest)).thenReturn(mockArticleComment);
        //when
        int result = commentService.saveComment(articleId, mockCommentRequest);
        //then
        assertThat(result).isEqualTo(commentId);
    }

    @Test
    void saveCommentReply() {
        //given
        Mockito.when(articleCommentRepository.findById(commentId)).thenReturn(Optional.ofNullable(mockArticleComment));
        Mockito.when(articleCommentRepository.save(any(ArticleComment.class))).thenReturn(mockReply);
        Mockito.when(commentDtoManager.fromRequestToEntityWithParent(commentId, mockReplyRequest)).thenReturn(mockReply);
        //when
        int result = commentService.saveCommentReply(articleId, commentId, mockReplyRequest);
        //then
        assertThat(result).isEqualTo(replyId);
    }

    @Test
    void findComments() {
        //given
        Mockito.when(boardRepository.findById(articleId)).thenReturn(Optional.ofNullable(mockArticle));
        Mockito.when(articleCommentRepository.findAllByArticle(mockArticle)).thenReturn(mockCommentList);
        Mockito.when(commentDtoManager.fromEntityToResponse(memberId, mockArticleComment)).thenReturn(mockCommentResponse);
        //when
        List<CommentResponse> result = commentService.findComments(memberId, articleId);
        //then

    }

    @Test
    void modifyComment() {
        //given
        //when
        //then
    }

    @Test
    void removeComment() {
        //given
        //when
        //then
    }

    @Test
    void saveCommentLike() {
        //given
        //when
        //then
    }

    @Test
    void removeCommentLike() {
        //given
        //when
        //then
    }

}