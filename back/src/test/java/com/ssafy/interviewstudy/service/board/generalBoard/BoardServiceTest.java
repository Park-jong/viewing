package com.ssafy.interviewstudy.service.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.exception.board.NotFoundException;
import com.ssafy.interviewstudy.repository.board.generalBoard.BoardRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private BoardDtoManager boardDtoManager;
    @Mock
    private BoardFileManager boardFileService;
    @InjectMocks
    private BoardServiceImpl boardService;

    Member mockMember;
    Board mockBoard;
    Pageable pageable;
    List<Board> mockBoardList;
    BoardType mockBoardType;
    BoardResponse mockBoardResponse;
    BoardResponse mockModifyBoardResponse;
    BoardRequest mockModifyRequest;
    BoardRequest mockRequest;

    final int memberId = 1;
    final int articleId = 1;

    @BeforeEach
    void setUp() {
        mockMember = createMockMember();
        mockBoard = createMockBoard(mockMember);
        mockBoardType = BoardType.general;
        mockBoardList = new ArrayList<>();
        mockBoardList.add(mockBoard);
        pageable = PageRequest.of(0, 10);
        mockBoardResponse = createMockBoardResponse(mockMember);
        mockModifyBoardResponse = modifyMockBoardResponse(mockMember);
        mockRequest = new BoardRequest(memberId, null, "제목", "내용", BoardType.general, null);
        mockModifyRequest = new BoardRequest(memberId, null, "제목 수정", "내용 수정", BoardType.general, null);
    }

    private Member createMockMember() {
        return Member.builder().id(memberId).nickname("홍길동").build();
    }

    private Board createMockBoard(Member author) {
        Board board = new Board();
        board.setId(articleId);
        board.setTitle("제목 관련");
        board.setContent("내용 관련");
        board.setAuthor(author);
        board.setViewCount(0);
        return board;
    }

    private BoardResponse createMockBoardResponse(Member author) {
        return BoardResponse.builder()
                .articleId(articleId)
                .author(new Author(author))
                .title("제목 관련")
                .content("내용 관련")
                .viewCount(0)
                .build();
    }

    private BoardResponse modifyMockBoardResponse(Member author) {
        return BoardResponse.builder()
                .articleId(articleId)
                .author(new Author(author))
                .title("제목 수정")
                .content("내용 수정")
                .viewCount(0)
                .build();
    }

    // ── 검색 ──────────────────────────────────────────────

    @Test
    void findArticleByKeywordWithTitle() {
        Mockito.when(boardRepository.findByTitleContaining("제목", mockBoardType, pageable))
                .thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        Mockito.when(boardDtoManager.fromEntityWithoutContent(mockBoard)).thenReturn(mockBoardResponse);

        Page<BoardResponse> result = boardService.findArticleByKeyword("title", "제목", mockBoardType, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).contains("제목");
    }

    @Test
    void findArticleByKeywordWithContent() {
        Mockito.when(boardRepository.findByTitleOrContent("내용", mockBoardType, pageable))
                .thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        Mockito.when(boardDtoManager.fromEntityWithoutContent(mockBoard)).thenReturn(mockBoardResponse);

        Page<BoardResponse> result = boardService.findArticleByKeyword("content", "내용", mockBoardType, pageable);

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void findArticleByKeywordWithAuthor() {
        Mockito.when(boardRepository.findWithAuthor("홍길", mockBoardType, pageable))
                .thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        Mockito.when(boardDtoManager.fromEntityWithoutContent(mockBoard)).thenReturn(mockBoardResponse);

        Page<BoardResponse> result = boardService.findArticleByKeyword("author", "홍길", mockBoardType, pageable);

        assertThat(result.getContent())
                .hasSize(1)
                .extracting(r -> r.getAuthor().getNickname())
                .containsExactly("홍길동");
    }

    @Test
    void findArticleByKeyword_unknownSearchBy_returnsEmpty() {
        Page<BoardResponse> result = boardService.findArticleByKeyword("unknown", "키워드", mockBoardType, pageable);

        assertThat(result.getContent()).isEmpty();
    }

    // ── 목록 조회 ──────────────────────────────────────────

    @Test
    void findBoardList() {
        Mockito.when(boardRepository.findByType(mockBoardType, pageable))
                .thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        Mockito.when(boardDtoManager.fromEntityWithoutContent(mockBoard)).thenReturn(mockBoardResponse);

        Page<BoardResponse> result = boardService.findBoardList(mockBoardType, pageable);

        assertThat(result).isNotEmpty();
    }

    // ── 단건 조회 ──────────────────────────────────────────

    @Test
    void findArticle() {
        Mockito.when(boardRepository.findById(articleId)).thenReturn(Optional.of(mockBoard));
        Mockito.when(boardDtoManager.fromEntityToResponse(memberId, mockBoard)).thenReturn(mockBoardResponse);

        BoardResponse result = boardService.findArticle(memberId, articleId, mockBoardType);

        assertThat(result)
                .extracting(BoardResponse::getArticleId, r -> r.getAuthor().getNickname())
                .containsExactly(articleId, "홍길동");
    }

    @Test
    void findArticle_notFound_throwsException() {
        Mockito.when(boardRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.findArticle(memberId, 999, mockBoardType))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void findArticle_incrementsViewCount() {
        int initialViewCount = mockBoard.getViewCount();
        Mockito.when(boardRepository.findById(articleId)).thenReturn(Optional.of(mockBoard));
        Mockito.when(boardDtoManager.fromEntityToResponse(memberId, mockBoard)).thenReturn(mockBoardResponse);

        boardService.findArticle(memberId, articleId, mockBoardType);

        assertThat(mockBoard.getViewCount()).isEqualTo(initialViewCount + 1);
    }

    // ── 저장 ──────────────────────────────────────────────

    @Test
    void saveArticle() {
        Mockito.when(boardDtoManager.fromRequestToEntity(mockRequest)).thenReturn(mockBoard);
        Mockito.when(boardRepository.save(any(Board.class))).thenReturn(mockBoard);

        Integer result = boardService.saveArticle(mockRequest, null);

        assertThat(result).isEqualTo(articleId);
    }

    // ── 수정 ──────────────────────────────────────────────

    @Test
    void modifyArticle() {
        Mockito.when(boardRepository.findById(articleId)).thenReturn(Optional.of(mockBoard));
        Mockito.when(boardDtoManager.fromEntityToResponse(memberId, mockBoard)).thenReturn(mockModifyBoardResponse);

        BoardResponse result = boardService.modifyArticle(articleId, mockModifyRequest, null);

        assertThat(result.getTitle()).isEqualTo("제목 수정");
        assertThat(result.getContent()).isEqualTo("내용 수정");
    }

    @Test
    void modifyArticle_notFound_throwsException() {
        Mockito.when(boardRepository.findById(999)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> boardService.modifyArticle(999, mockModifyRequest, null))
                .isInstanceOf(NotFoundException.class);
    }

    // ── 삭제 ──────────────────────────────────────────────

    @Test
    void removeArticle() {
        Mockito.when(boardRepository.findById(articleId)).thenReturn(Optional.of(mockBoard));

        Integer result = boardService.removeArticle(articleId);

        assertThat(result).isEqualTo(articleId);
    }

    @Test
    void removeArticle_notFound_returnsZero() {
        Mockito.when(boardRepository.findById(999)).thenReturn(Optional.empty());

        Integer result = boardService.removeArticle(999);

        assertThat(result).isEqualTo(0);
    }

    // ── 작성자 확인 ────────────────────────────────────────

    @Test
    void checkAuthor_isAuthor_returnsTrue() {
        Mockito.when(boardRepository.findById(articleId)).thenReturn(Optional.of(mockBoard));

        Boolean result = boardService.checkAuthor(articleId, memberId);

        assertThat(result).isTrue();
    }

    @Test
    void checkAuthor_isNotAuthor_returnsFalse() {
        Mockito.when(boardRepository.findById(articleId)).thenReturn(Optional.of(mockBoard));

        Boolean result = boardService.checkAuthor(articleId, 999);

        assertThat(result).isFalse();
    }
}
