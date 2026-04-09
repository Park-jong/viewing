package com.ssafy.interviewstudy.service.board.studyBoard;

import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.board.StudyBoardComment;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.StudyBoardCommentResponse;
import com.ssafy.interviewstudy.exception.board.NotFoundException;
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

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
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

    Member mockMember;
    Study mockStudy;
    StudyBoard mockArticle;
    StudyBoardComment mockComment;
    StudyBoardComment mockReply;
    CommentRequest mockCommentRequest;
    CommentRequest mockReplyRequest;
    CommentRequest mockUpdateRequest;
    StudyBoardCommentResponse mockCommentResponse;
    StudyBoardCommentResponse mockReplyResponse;
    StudyBoardCommentResponse mockUpdateResponse;

    final int studyId = 5;
    final int articleId = 2;
    final int memberId = 3;
    final int commentId = 1;
    final int replyId = 4;

    @BeforeEach
    void setUp() {
        mockMember = Member.builder().id(memberId).nickname("홍길동").build();
        mockStudy = Study.builder().id(studyId).title("스터디명").leader(mockMember).build();
        mockArticle = StudyBoard.builder()
                .id(articleId).study(mockStudy).author(mockMember)
                .title("기본 제목").content("기본 내용").build();

        mockComment = new StudyBoardComment();
        mockComment.setId(commentId);
        mockComment.setArticle(mockArticle);
        mockComment.setAuthor(mockMember);
        mockComment.setContent("댓글 내용");
        mockComment.setIsDelete(false);

        mockReply = new StudyBoardComment();
        mockReply.setId(replyId);
        mockReply.setArticle(mockArticle);
        mockReply.setAuthor(mockMember);
        mockReply.setContent("대댓글 내용");
        mockReply.setComment(mockComment);
        mockReply.setIsDelete(false);

        mockCommentRequest = createRequest("댓글 내용");
        mockReplyRequest = createRequest("대댓글 내용");
        mockUpdateRequest = createRequest("댓글 수정");

        mockCommentResponse = StudyBoardCommentResponse.builder().content("댓글 내용").build();
        mockReplyResponse = StudyBoardCommentResponse.builder().content("대댓글 내용").build();
        mockUpdateResponse = StudyBoardCommentResponse.builder().content("댓글 수정").build();
    }

    private CommentRequest createRequest(String content) {
        CommentRequest req = new CommentRequest();
        req.setArticleId(articleId);
        req.setMemberId(memberId);
        req.setContent(content);
        return req;
    }

    // ── 댓글 저장 ──────────────────────────────────────────

    @Test
    void saveComment() {
        Mockito.when(commentDtoManager.toEntity(mockCommentRequest)).thenReturn(mockComment);
        Mockito.when(commentRepository.save(any(StudyBoardComment.class))).thenReturn(mockComment);

        int result = studyBoardCommentService.saveComment(articleId, mockCommentRequest);

        assertThat(result).isEqualTo(commentId);
    }

    // ── 대댓글 저장 ────────────────────────────────────────

    @Test
    void saveCommentReply() {
        // mockReply.getComment() == mockComment (부모 댓글)
        // sendNotificationAboutReply 에서 mockComment.getArticle().getStudy() 접근
        Mockito.when(commentDtoManager.toEntityWithParent(commentId, mockReplyRequest)).thenReturn(mockReply);
        Mockito.when(commentRepository.save(any(StudyBoardComment.class))).thenReturn(mockReply);

        int result = studyBoardCommentService.saveCommentReply(articleId, commentId, mockReplyRequest);

        assertThat(result).isEqualTo(replyId);
        // 이중 조회 없이 toEntityWithParent 한 번만 호출됐는지 확인
        verify(commentDtoManager, times(1)).toEntityWithParent(commentId, mockReplyRequest);
        verify(commentRepository, times(0)).findById(commentId);
    }

    @Test
    void saveCommentReply_commentNotFound_throwsException() {
        Mockito.when(commentDtoManager.toEntityWithParent(999, mockReplyRequest))
                .thenThrow(new NotFoundException("해당하는 댓글이 존재하지 않습니다."));

        assertThatThrownBy(() -> studyBoardCommentService.saveCommentReply(articleId, 999, mockReplyRequest))
                .isInstanceOf(NotFoundException.class);
    }

    // ── 댓글 목록 조회 ─────────────────────────────────────

    @Test
    void findComments() {
        Mockito.when(boardRepository.findById(articleId)).thenReturn(Optional.of(mockArticle));
        Mockito.when(commentRepository.findAllByArticle(mockArticle)).thenReturn(List.of(mockComment, mockReply));
        Mockito.when(commentDtoManager.fromEntity(mockComment)).thenReturn(mockCommentResponse);
        Mockito.when(commentDtoManager.fromEntity(mockReply)).thenReturn(mockReplyResponse);

        List<StudyBoardCommentResponse> result = studyBoardCommentService.findComments(articleId);

        assertThat(result).hasSize(2);
    }

    @Test
    void findComments_articleNotFound_throwsException() {
        Mockito.when(boardRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyBoardCommentService.findComments(999))
                .isInstanceOf(NotFoundException.class);
    }

    // ── 댓글 수정 ──────────────────────────────────────────

    @Test
    void modifyComment() {
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));
        Mockito.when(commentDtoManager.fromEntity(mockComment)).thenReturn(mockUpdateResponse);

        StudyBoardCommentResponse result = studyBoardCommentService.modifyComment(commentId, mockUpdateRequest);

        assertThat(result.getContent()).isEqualTo("댓글 수정");
        // 관리 엔티티이므로 save 호출 없음
        verify(commentRepository, times(0)).save(any());
    }

    @Test
    void modifyComment_notFound_throwsException() {
        Mockito.when(commentRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyBoardCommentService.modifyComment(999, mockUpdateRequest))
                .isInstanceOf(NotFoundException.class);
    }

    // ── 댓글 삭제 (소프트 삭제) ────────────────────────────

    @Test
    void removeComment() {
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        studyBoardCommentService.removeComment(commentId);

        assertThat(mockComment.getIsDelete()).isTrue();
        verify(commentRepository, times(1)).findById(commentId);
        // 관리 엔티티이므로 save 호출 없음
        verify(commentRepository, times(0)).save(any());
    }

    @Test
    void removeComment_notFound_throwsException() {
        Mockito.when(commentRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyBoardCommentService.removeComment(999))
                .isInstanceOf(NotFoundException.class);
    }

    // ── 작성자 확인 ────────────────────────────────────────

    @Test
    void checkAuthor_isAuthor_returnsTrue() {
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        Boolean result = studyBoardCommentService.checkAuthor(commentId, memberId);

        assertThat(result).isTrue();
    }

    @Test
    void checkAuthor_isNotAuthor_returnsFalse() {
        Mockito.when(commentRepository.findById(commentId)).thenReturn(Optional.of(mockComment));

        Boolean result = studyBoardCommentService.checkAuthor(commentId, 999);

        assertThat(result).isFalse();
    }

    @Test
    void checkAuthor_notFound_throwsException() {
        Mockito.when(commentRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyBoardCommentService.checkAuthor(999, memberId))
                .isInstanceOf(NotFoundException.class);
    }
}
