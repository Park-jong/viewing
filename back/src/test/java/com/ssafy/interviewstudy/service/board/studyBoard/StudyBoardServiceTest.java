package com.ssafy.interviewstudy.service.board.studyBoard;

import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.StudyBoardResponse;
import com.ssafy.interviewstudy.repository.board.studyBoard.StudyBoardRepository;
import com.ssafy.interviewstudy.service.board.generalBoard.BoardFileService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
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
    private BoardFileService boardFileService;
    @InjectMocks
    private StudyBoardServiceImpl studyBoardService;


    Member mockMember;
    Study mockStudy;
    StudyBoard mockBoard;
    Pageable pageable;
    List<StudyBoard> mockBoardList;
    BoardType mockBoardType;
    StudyBoardResponse mockBoardResponse;
    StudyBoardResponse mockModifyBoardResponse;
    BoardRequest mockModifyRequest;
    BoardRequest mockRequest;
    int memberId = 1;
    int studyId = 2;
    int boardId = 3;
    String memberName = "홍길동";
    String title = "기본 제목";
    String content = "기본 내용";
    String m_title = "제목 수정";
    String m_content = "내용 수정";

    @BeforeEach
    void setUp() {
        mockMember = createMockMember();
        mockStudy = createMockStudy();
        mockBoard = createMockBoard(mockMember);
        mockBoardType = BoardType.general;
        mockBoardList = new ArrayList<>();
        mockBoardList.add(mockBoard);
        pageable = PageRequest.of(0, 10);
        mockBoardResponse = createMockBoardResponse(mockMember);
        mockModifyBoardResponse = modifyMockBoardResponse(mockMember);
        mockRequest = createMockRequest();
        mockModifyRequest = modifyMockRequest();
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
                .id(boardId)
                .title(title)
                .content(content)
                .author(author)
                .build();
    }

    private StudyBoardResponse createMockBoardResponse(Member author) {
        return StudyBoardResponse.builder()
                .articleId(boardId)
                .author(new Author(author))
                .title(title)
                .content(content)
                .build();
    }

    private StudyBoardResponse modifyMockBoardResponse(Member author) {
        return StudyBoardResponse.builder()
                .articleId(boardId)
                .author(new Author(author))
                .title(m_title)
                .content(m_content)
                .build();
    }

    private BoardRequest createMockRequest() {
        return new BoardRequest(memberId, studyId, title, content, BoardType.general, null);
    }

    private BoardRequest modifyMockRequest() {
        return new BoardRequest(memberId, studyId, m_title, m_content, BoardType.general, null);
    }


    @Test
    void findBoardList() {
        //given
        Mockito.when(boardRepository.findByStudyId(studyId, pageable)).thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        //when
        Page<StudyBoardResponse> result = studyBoardService.findBoardList(studyId, pageable);
        //then
        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void findArticle() {
        //given
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(mockBoard));
        Mockito.when(boardDtoManager.fromEntity(mockBoard)).thenReturn(mockBoardResponse);
        //when
        StudyBoardResponse result = studyBoardService.findArticle(boardId);
        //then
        assertThat(result)
                .extracting(StudyBoardResponse::getArticleId, response -> response.getAuthor().getNickname(), StudyBoardResponse::getTitle, StudyBoardResponse::getContent)
                .containsExactly(boardId, memberName, title, content);
    }

    @Test
    void modifyArticle() {
        //given
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(mockBoard));
        Mockito.when(boardDtoManager.fromEntity(mockBoard)).thenReturn(mockModifyBoardResponse);
        //when
        StudyBoardResponse result = studyBoardService.modifyArticle(boardId, mockModifyRequest, null);
        //then
        assertThat(result.getArticleId()).isEqualTo(boardId);
        assertThat(result.getTitle()).isEqualTo(m_title);
        assertThat(result.getContent()).isEqualTo(m_content);
    }

    @Test
    void removeArticle() {
        //given
        Mockito.when(boardRepository.findById(boardId)).thenReturn(Optional.ofNullable(mockBoard));
        //when
        Integer result = studyBoardService.removeArticle(boardId);
        //then
        assertThat(result).isEqualTo(boardId);
    }

    @Test
    void saveBoard() {
        //given
        Mockito.when(boardRepository.save(any(StudyBoard.class))).thenReturn(mockBoard);
        Mockito.when(boardDtoManager.toEntity(mockRequest)).thenReturn(mockBoard);
        //when
        Integer result = studyBoardService.saveBoard(mockRequest, null);
        //then
        assertThat(result).isEqualTo(boardId);
    }

    @Test
    void findArticleByKeywordWithAuthor() {
        //given
        String searchBy = "author";
        String mockKeyword = "홍길";
        Mockito.when(boardRepository.findWithAuthor(studyId, mockKeyword, pageable)).thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        Mockito.when(boardDtoManager.fromEntityWithoutContent(mockBoard)).thenReturn(mockBoardResponse);
        //when
        Page<StudyBoardResponse> result = studyBoardService.findArticleByKeyword(studyId, searchBy, mockKeyword, pageable);
        //then
        assertThat(result.getContent())
                .hasSize(1)
                .extracting(response -> response.getAuthor().getNickname())
                .containsExactly(memberName);

    }

    @Test
    void findArticleByKeywordWithTitle() {
        //given
        String searchBy = "title";
        String mockKeyword = "제목";
        Mockito.when(boardRepository.findByTitleContaining(studyId, mockKeyword, pageable)).thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        Mockito.when(boardDtoManager.fromEntityWithoutContent(mockBoard)).thenReturn(mockBoardResponse);
        //when
        Page<StudyBoardResponse> result = studyBoardService.findArticleByKeyword(studyId, searchBy, mockKeyword, pageable);
        //then
        assertThat(result.getContent())
                .hasSize(1)
                .extracting(StudyBoardResponse::getTitle)
                .allMatch(t -> t.contains(title));
    }

    @Test
    void findArticleByKeywordWithContent() {
        //given
        String searchBy = "content";
        String mockKeyword = "내용";
        Mockito.when(boardRepository.findByTitleOrContent(studyId, mockKeyword, pageable)).thenReturn(new PageImpl<>(mockBoardList, pageable, 1));
        Mockito.when(boardDtoManager.fromEntityWithoutContent(mockBoard)).thenReturn(mockBoardResponse);
        //when
        Page<StudyBoardResponse> result = studyBoardService.findArticleByKeyword(studyId, searchBy, mockKeyword, pageable);
        //then
        assertThat(result.getContent())
                .hasSize(1)
                .extracting(StudyBoardResponse::getContent)
                .allMatch(c -> c.contains(content));
    }
}