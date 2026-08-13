package com.ssafy.interviewstudy.service.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.board.CommentLike;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.CommentResponse;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.board.generalBoard.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.generalBoard.BoardRepository;
import com.ssafy.interviewstudy.repository.board.generalBoard.CommentLikeRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

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
    @InjectMocks
    private CommentServiceImpl commentService;

    Member mockMember;
    Board mockArticle;
    ArticleComment mockComment;
    ArticleComment mockReply;
    CommentRequest mockCommentRequest;
    CommentRequest mockReplyRequest;
    CommentRequest mockUpdateRequest;
    CommentResponse mockCommentResponse;
    CommentResponse mockReplyResponse;
    CommentResponse mockUpdateResponse;

    final int articleId = 2;
    final int memberId = 3;
    final int commentId = 1;
    final int replyId = 4;

    @BeforeEach
    void setUp() {
        mockMember = Member.builder().id(memberId).nickname("홍길동").build();
        mockArticle = createMockBoard();
        mockComment = createMockComment();
        mockReply = createMockReply();

        mockCommentRequest = createRequest(articleId, memberId, "댓글 내용");
        mockReplyRequest = createRequest(articleId, memberId, "대댓글 내용");
        mockUpdateRequest = createRequest(articleId, memberId, "댓글 수정");

        mockCommentResponse = responseOf("댓글 내용");
        mockReplyResponse = responseOf("대댓글 내용");
        mockUpdateResponse = responseOf("댓글 수정");
    }

    private Board createMockBoard() {
        Board board = new Board();
        board.setId(articleId);
        board.setTitle("제목");
        board.setContent("내용");
        board.setAuthor(mockMember);
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
        comment.setIsDelete(false);
        return comment;
    }

    private ArticleComment createMockReply() {
        ArticleComment reply = new ArticleComment();
        reply.setId(replyId);
        reply.setArticle(mockArticle);
        reply.setAuthor(mockMember);
        reply.setContent("대댓글 내용");
        reply.setComment(mockComment);
        reply.setIsDelete(false);
        return reply;
    }

    private CommentRequest createRequest(int articleId, int memberId, String content) {
        CommentRequest req = new CommentRequest();
        req.setArticleId(articleId);
        req.setMemberId(memberId);
        req.setContent(content);
        return req;
    }

    private CommentResponse responseOf(String content) {
        CommentResponse res = new CommentResponse();
        res.setContent(content);
        return res;
    }

    // ── 댓글 저장 ──────────────────────────────────────────

    @Test
    void saveComment() {
        Mockito.when(commentDtoManager.fromRequestToEntity(mockCommentRequest)).thenReturn(mockComment);
        Mockito.when(articleCommentRepository.save(any(ArticleComment.class))).thenReturn(mockComment);

        int result = commentService.saveComment(articleId, mockCommentRequest);

        assertThat(result).isEqualTo(commentId);
    }

    // ── 대댓글 저장 ────────────────────────────────────────

    @Test
    void saveCommentReply() {
        Mockito.when(commentDtoManager.fromRequestToEntityWithParent(commentId, mockReplyRequest)).thenReturn(mockReply);
        Mockito.when(articleCommentRepository.save(any(ArticleComment.class))).thenReturn(mockReply);

        int result = commentService.saveCommentReply(articleId, commentId, mockReplyRequest);

        assertThat(result).isEqualTo(replyId);
    }

    @Test
    void saveCommentReply_commentNotFound_throwsException() {
        Mockito.when(commentDtoManager.fromRequestToEntityWithParent(999, mockReplyRequest))
                .thenThrow(new NotFoundException("해당하는 댓글이 존재하지 않습니다."));

        assertThatThrownBy(() -> commentService.saveCommentReply(articleId, 999, mockReplyRequest))
                .isInstanceOf(NotFoundException.class);
    }

    // ── 댓글 목록 조회 ─────────────────────────────────────

    @Test
    void findComments() {
        List<ArticleComment> commentList = List.of(mockComment, mockReply);
        List<CommentResponse> responseList = List.of(mockCommentResponse, mockReplyResponse);

        Mockito.when(boardRepository.findById(articleId)).thenReturn(Optional.of(mockArticle));
        Mockito.when(articleCommentRepository.findAllByArticle(mockArticle)).thenReturn(commentList);
        Mockito.when(commentDtoManager.fromEntitiesToResponses(memberId, commentList)).thenReturn(responseList);

        List<CommentResponse> result = commentService.findComments(memberId, articleId);

        assertThat(result).hasSize(2);
    }

    @Test
    void findComments_articleNotFound_throwsException() {
        Mockito.when(boardRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.findComments(memberId, 999))
                .isInstanceOf(NotFoundException.class);
    }

    // ── 댓글 수정 ──────────────────────────────────────────

    @Test
    void modifyComment() {
        Mockito.when(articleCommentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
        Mockito.when(commentDtoManager.fromEntityToResponse(memberId, mockComment)).thenReturn(mockUpdateResponse);

        CommentResponse result = commentService.modifyComment(commentId, mockUpdateRequest);

        assertThat(result.getContent()).isEqualTo("댓글 수정");
    }

    @Test
    void modifyComment_notFound_throwsException() {
        Mockito.when(articleCommentRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.modifyComment(999, mockUpdateRequest))
                .isInstanceOf(NotFoundException.class);
    }

    // ── 댓글 삭제 (소프트 삭제) ────────────────────────────

    @Test
    void removeComment() {
        Mockito.when(articleCommentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        commentService.removeComment(commentId);

        assertThat(mockComment.getIsDelete()).isTrue();
        verify(articleCommentRepository, times(1)).findById(commentId);
    }

    @Test
    void removeComment_notFound_throwsException() {
        Mockito.when(articleCommentRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> commentService.removeComment(999))
                .isInstanceOf(NotFoundException.class);
    }

    // ── 댓글 좋아요 ────────────────────────────────────────

    @Test
    void saveCommentLike() {
        Mockito.when(memberRepository.findMemberById(memberId)).thenReturn(Optional.of(mockMember));
        Mockito.when(articleCommentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
        Mockito.when(commentLikeRepository.existsByMemberIdAndCommentId(memberId, commentId)).thenReturn(false);
        CommentLike mockLike = CommentLike.builder().id(memberId).member(mockMember).comment(mockComment).build();
        Mockito.when(commentLikeRepository.save(any(CommentLike.class))).thenReturn(mockLike);

        Integer result = commentService.saveCommentLike(memberId, commentId);

        assertThat(result).isNotEqualTo(0);
    }

    @Test
    void saveCommentLike_alreadyLiked_returnsZero() {
        Mockito.when(memberRepository.findMemberById(memberId)).thenReturn(Optional.of(mockMember));
        Mockito.when(articleCommentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
        Mockito.when(commentLikeRepository.existsByMemberIdAndCommentId(memberId, commentId)).thenReturn(true);

        Integer result = commentService.saveCommentLike(memberId, commentId);

        assertThat(result).isEqualTo(0);
    }

    // ── 작성자 확인 ────────────────────────────────────────

    @Test
    void checkAuthor_isAuthor_returnsTrue() {
        Mockito.when(articleCommentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        Boolean result = commentService.checkAuthor(commentId, memberId);

        assertThat(result).isTrue();
    }

    @Test
    void checkAuthor_isNotAuthor_returnsFalse() {
        Mockito.when(articleCommentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        Boolean result = commentService.checkAuthor(commentId, 999);

        assertThat(result).isFalse();
    }
}
