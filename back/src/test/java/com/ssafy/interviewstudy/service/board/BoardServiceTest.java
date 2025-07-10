package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.dto.board.FileResponse;
import com.ssafy.interviewstudy.repository.board.ArticleFileRepository;
import com.ssafy.interviewstudy.repository.board.BoardRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.service.redis.ArticleLikeService;
import com.ssafy.interviewstudy.support.file.FileManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BoardServiceTest {

    @Mock
    private BoardRepository boardRepository;
    @Mock
    private ArticleFileRepository articleFileRepository;
    @Mock
    private ArticleLikeService articleLikeService;
    @Mock
    private MemberRepository memberRepository;
    @InjectMocks
    private BoardService boardService;

    Member mockMember;
    Board mockBoard;
    Pageable pageable;
    List<Board> mockBoardList;
    BoardType mockBoardType;
    BoardResponse mockBoardResponse;

    @BeforeEach
    void setUp() {
        mockMember = createMockMember();
        mockBoard = createMockBoard(mockMember);
        mockBoardType = BoardType.general;
        mockBoardList = new ArrayList<>();
        mockBoardList.add(mockBoard);
        pageable = PageRequest.of(0, 10);
        mockBoardResponse = createMockBoardResponse(mockMember);
    }

    private Member createMockMember() {
        return Member.builder()
                .id(1)
                .nickname("홍길동")
                .build();
    }

    private Board createMockBoard(Member author) {
        Board board = new Board();
        board.setId(1);
        board.setTitle("제목 관련");
        board.setContent("내용 관련");
        board.setAuthor(author);
        board.setViewCount(0);
        return board;
    }

    private BoardResponse createMockBoardResponse(Member author) {
        return BoardResponse.builder()
                .articleId(1)
                .author(new Author(author))
                .title("제목 관련")
                .content("내용 관련")
                .viewCount(0)
                .build();
    }

    @Test
    void findArticleByKeywordWithAuthor() {
        //given
        String searchBy = "author";
        String mockKeyword = "홍길";
        mockBoard.setAuthor(mockMember);
        Mockito.when(boardRepository.findWithAuthor(mockKeyword, mockBoardType, pageable)).thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        Mockito.when(articleLikeService.getLikeCount(1)).thenReturn(20);
        //when
        Page<BoardResponse> result = boardService.findArticleByKeyword(searchBy, mockKeyword, mockBoardType, pageable);
        //then
        assertThat(result.getContent())
                .hasSize(1)
                .extracting(response -> response.getAuthor().getNickname())
                .containsExactly("홍길동");

    }

    @Test
    void findArticleByKeywordWithTitle() {
        //given
        String searchBy = "title";
        String mockKeyword = "제목";
        Mockito.when(boardRepository.findByTitleContaining(mockKeyword, mockBoardType, pageable)).thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        Mockito.when(articleLikeService.getLikeCount(1)).thenReturn(20);
        //when
        Page<BoardResponse> result = boardService.findArticleByKeyword(searchBy, mockKeyword, mockBoardType, pageable);
        //then
        assertThat(result.getContent())
                .hasSize(1)
                .extracting(BoardResponse::getTitle)
                .allMatch(title -> title.contains("제목"));
    }

    @Test
    void findArticleByKeywordWithContent() {
        //given
        String searchBy = "content";
        String mockKeyword = "내용";
        Mockito.when(boardRepository.findByTitleOrContent(mockKeyword, mockBoardType, pageable)).thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        Mockito.when(articleLikeService.getLikeCount(1)).thenReturn(20);
        //when
        Page<BoardResponse> result = boardService.findArticleByKeyword(searchBy, mockKeyword, mockBoardType, pageable);
        //then
        assertThat(result.getContent())
                .hasSize(1)
                .extracting(BoardResponse::getTitle)
                .allMatch(title -> title.contains("제목"));
    }

    @Test
    void findBoardList() {
        //given
        Mockito.when(boardRepository.findByType(mockBoardType, pageable)).thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        //when
        Page<BoardResponse> result = boardService.findBoardList(mockBoardType, pageable);
        //then
        assertThat(result).isNotEmpty();
    }

    @Test
    void findArticle() {
        //given
        Mockito.when(boardRepository.findById(1)).thenReturn(Optional.ofNullable(mockBoard));
        Mockito.when(articleLikeService.getLikeCount(1)).thenReturn(20);
        Mockito.when(articleLikeService.checkMemberLikeArticle(1, 1)).thenReturn(true);
        //when
        BoardResponse result = boardService.findArticle(1, 1, mockBoardType);
        //then
        assertThat(result)
                .extracting(BoardResponse::getArticleId, response -> response.getAuthor().getNickname())
                .containsExactly(1, "홍길동");
    }

    @Test
    void modifyArticle() {
        //given
        BoardRequest mockRequest = new BoardRequest(1, 1, "제목 수정", "내용 수정", BoardType.general, null);
        Mockito.when(boardRepository.findById(1)).thenReturn(Optional.ofNullable(mockBoard));
        Mockito.when(articleLikeService.getLikeCount(1)).thenReturn(20);
        Mockito.when(articleLikeService.checkMemberLikeArticle(1, 1)).thenReturn(true);
        //when
        BoardResponse result = boardService.modifyArticle(1, mockRequest, null);
        //then
        assertThat(result.getArticleId()).isEqualTo(1);
        assertThat(result.getTitle()).isEqualTo("제목 수정");
        assertThat(result.getContent()).isEqualTo("내용 수정");
    }

    @Test
    void removeArticle() {
        //given
        Mockito.when(boardRepository.findById(1)).thenReturn(Optional.ofNullable(mockBoard));
        //when
        Integer result = boardService.removeArticle(1);
        //then
        assertThat(result).isEqualTo(1);
    }


    @Test
    void saveBoard() {
        //given
        BoardRequest boardRequest = new BoardRequest(1, null, "제목", "내용", BoardType.general, null);
        Board output = new Board();
        output.setId(1);
        output.setTitle("제목");
        output.setContent("내용");
        output.setAuthor(mockMember);
        output.setViewCount(0);
        Mockito.when(boardRepository.save(any(Board.class))).thenReturn(output);
        Mockito.when(memberRepository.findMemberById(1)).thenReturn(mockMember);
        //when
        Integer result = boardService.saveBoard(boardRequest, null);
        //then
        assertThat(result).isEqualTo(1);
    }

    @Test
    void modifyViewCount() {
        //given
        int viewCount = mockBoard.getViewCount();
        //when
        boardService.modifyViewCount(mockBoard);
        //then
        assertThat(mockBoard.getViewCount()).isEqualTo(viewCount + 1);
    }
}