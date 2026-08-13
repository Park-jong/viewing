package com.ssafy.interviewstudy.service.board.studyBoard;

import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.StudyBoardResponse;
import com.ssafy.interviewstudy.exception.message.NotFoundException;
import com.ssafy.interviewstudy.repository.board.studyBoard.StudyBoardRepository;
import com.ssafy.interviewstudy.service.board.generalBoard.BoardFileManager;
import com.ssafy.interviewstudy.service.notification.NotificationDtoManager;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class StudyBoardServiceTest {

    @Mock
    private StudyBoardRepository boardRepository;
    @Mock
    private StudyBoardDtoManager boardDtoManager;
    @Mock
    private NotificationService notificationService;
    @Mock
    private NotificationDtoManager notificationDtoManager;
    @Mock
    private BoardFileManager boardFileService;
    @InjectMocks
    private StudyBoardServiceImpl studyBoardService;

    Member mockMember;
    Study mockStudy;
    StudyBoard mockBoard;
    Pageable pageable;
    StudyBoardResponse mockBoardResponse;
    StudyBoardResponse mockModifyBoardResponse;
    BoardRequest mockRequest;
    BoardRequest mockModifyRequest;

    final int memberId = 1;
    final int studyId = 2;
    final int boardId = 3;
    final String title = "기본 제목";
    final String content = "기본 내용";
    final String modifiedTitle = "제목 수정";
    final String modifiedContent = "내용 수정";

    @BeforeEach
    void setUp() {
        mockMember = Member.builder().id(memberId).nickname("홍길동").build();
        mockStudy = Study.builder().id(studyId).title("스터디명").leader(mockMember).build();
        mockBoard = StudyBoard.builder()
                .id(boardId).study(mockStudy).author(mockMember)
                .title(title).content(content).build();
        pageable = PageRequest.of(0, 10);
        mockBoardResponse = StudyBoardResponse.builder()
                .articleId(boardId).author(new Author(mockMember))
                .title(title).content(content).build();
        mockModifyBoardResponse = StudyBoardResponse.builder()
                .articleId(boardId).author(new Author(mockMember))
                .title(modifiedTitle).content(modifiedContent).build();
        mockRequest = new BoardRequest(memberId, studyId, title, content, null, null);
        mockModifyRequest = new BoardRequest(memberId, studyId, modifiedTitle, modifiedContent, null, null);
    }

    // ── 목록 조회 ──────────────────────────────────────────

    @Test
    void findBoardList() {
        Mockito.when(boardRepository.findByStudyId(studyId, pageable))
                .thenReturn(new PageImpl<>(List.of(mockBoard), pageable, 1));
        Mockito.when(boardDtoManager.fromEntityWithoutContent(mockBoard)).thenReturn(mockBoardResponse);

        Page<StudyBoardResponse> result = studyBoardService.findBoardList(studyId, pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    // ── 단건 조회 ──────────────────────────────────────────

    @Test
    void findArticle() {
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));
        Mockito.when(boardDtoManager.fromEntity(mockBoard)).thenReturn(mockBoardResponse);

        StudyBoardResponse result = studyBoardService.findArticle(boardId);

        assertThat(result)
                .extracting(StudyBoardResponse::getArticleId, r -> r.getAuthor().getNickname(),
                        StudyBoardResponse::getTitle, StudyBoardResponse::getContent)
                .containsExactly(boardId, "홍길동", title, content);
    }

    @Test
    void findArticle_notFound_throwsException() {
        Mockito.when(boardRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyBoardService.findArticle(999))
                .isInstanceOf(NotFoundException.class);
    }

    // ── 저장 ──────────────────────────────────────────────

    @Test
    void saveBoard() {
        Mockito.when(boardDtoManager.toEntity(mockRequest)).thenReturn(mockBoard);
        Mockito.when(boardRepository.save(any(StudyBoard.class))).thenReturn(mockBoard);

        Integer result = studyBoardService.saveBoard(mockRequest, null);

        assertThat(result).isEqualTo(boardId);
    }

    // ── 수정 ──────────────────────────────────────────────

    @Test
    void modifyArticle() {
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));
        Mockito.when(boardDtoManager.fromEntity(mockBoard)).thenReturn(mockModifyBoardResponse);

        StudyBoardResponse result = studyBoardService.modifyArticle(boardId, mockModifyRequest, null);

        assertThat(result.getTitle()).isEqualTo(modifiedTitle);
        assertThat(result.getContent()).isEqualTo(modifiedContent);
    }

    @Test
    void modifyArticle_notFound_throwsException() {
        Mockito.when(boardRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> studyBoardService.modifyArticle(999, mockModifyRequest, null))
                .isInstanceOf(NotFoundException.class);
    }

    // ── 삭제 ──────────────────────────────────────────────

    @Test
    void removeArticle() {
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));

        Integer result = studyBoardService.removeArticle(boardId);

        assertThat(result).isEqualTo(boardId);
    }

    @Test
    void removeArticle_notFound_returnsZero() {
        Mockito.when(boardRepository.findById(999)).thenReturn(Optional.empty());

        Integer result = studyBoardService.removeArticle(999);

        assertThat(result).isEqualTo(0);
    }

    // ── 검색 ──────────────────────────────────────────────

    @Test
    void findArticleByKeywordWithTitle() {
        Mockito.when(boardRepository.findByTitleContaining(studyId, "기본", pageable))
                .thenReturn(new PageImpl<>(List.of(mockBoard), pageable, 1));
        Mockito.when(boardDtoManager.fromEntityWithoutContent(mockBoard)).thenReturn(mockBoardResponse);

        Page<StudyBoardResponse> result = studyBoardService.findArticleByKeyword(studyId, "title", "기본", pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).contains("기본");
    }

    @Test
    void findArticleByKeywordWithContent() {
        Mockito.when(boardRepository.findByTitleOrContent(studyId, "내용", pageable))
                .thenReturn(new PageImpl<>(List.of(mockBoard), pageable, 1));
        Mockito.when(boardDtoManager.fromEntityWithoutContent(mockBoard)).thenReturn(mockBoardResponse);

        Page<StudyBoardResponse> result = studyBoardService.findArticleByKeyword(studyId, "content", "내용", pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void findArticleByKeywordWithAuthor() {
        Mockito.when(boardRepository.findWithAuthor(studyId, "홍길", pageable))
                .thenReturn(new PageImpl<>(List.of(mockBoard), pageable, 1));
        Mockito.when(boardDtoManager.fromEntityWithoutContent(mockBoard)).thenReturn(mockBoardResponse);

        Page<StudyBoardResponse> result = studyBoardService.findArticleByKeyword(studyId, "author", "홍길", pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .extracting(r -> r.getAuthor().getNickname())
                .containsExactly("홍길동");
    }

    // ── 작성자 확인 ────────────────────────────────────────

    @Test
    void checkAuthor_isAuthor_returnsTrue() {
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));

        Boolean result = studyBoardService.checkAuthor(boardId, memberId);

        assertThat(result).isTrue();
    }

    @Test
    void checkAuthor_isNotAuthor_returnsFalse() {
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.of(mockBoard));

        Boolean result = studyBoardService.checkAuthor(boardId, 999);

        assertThat(result).isFalse();
    }
}
