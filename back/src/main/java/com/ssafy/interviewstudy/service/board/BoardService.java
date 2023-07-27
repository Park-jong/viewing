package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.*;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.dto.board.StudyBoardRequest;
import com.ssafy.interviewstudy.repository.board.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    final int pageSize = 10;

    private BoardRepository boardRepository;
    private FreeBoardRepository freeBoardRepository;
    private InterviewReviewBoardRepository interviewReviewBoardRepository;
    private QuestionBoardRepository questionBoardRepository;
    private StudyBoardRepository studyBoardRepository;
    private BoardDtoService boardDtoService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public BoardService(BoardRepository boardRepository, FreeBoardRepository freeBoardRepository, InterviewReviewBoardRepository interviewReviewBoardRepository
            , QuestionBoardRepository questionBoardRepository, StudyBoardRepository studyBoardRepository, BoardDtoService boardDtoService) {
        this.boardRepository = boardRepository;
        this.freeBoardRepository = freeBoardRepository;
        this.interviewReviewBoardRepository = interviewReviewBoardRepository;
        this.questionBoardRepository = questionBoardRepository;
        this.studyBoardRepository = studyBoardRepository;
        this.boardDtoService = boardDtoService;
    }

    //글 리스트 조회, crud, 검색, 댓글 crud, 글 좋아요, 댓글 좋아요, 글 신고

    //글 목록 조회
    public List<BoardResponse> findBoardList(Integer memberId, String boardType, int page) {
        List<Board> boardList = boardRepository.findByType(boardType, PageRequest.of(page, pageSize)).getContent();
        List<BoardResponse> responseList = new ArrayList<>();

        for (Board b : boardList) {
            responseList.add(boardDtoService.fromEntityWithoutContent(memberId, b));
        }

        return responseList;
    }

    // 글 detail 조회
    public BoardResponse findArticle(Integer memberId, Integer articleId, String boardType) {
        Optional<Board> article = boardRepository.findById(articleId);
        // 페이지도 나중에 request 받기

        // Null이면 예외 발생 처리
        BoardResponse boardResponse = boardDtoService.fromEntity(memberId, article.orElseThrow(NullPointerException::new));
        boardResponse.setBoardType(boardType);

        return boardResponse;
    }

    // 글 수정
    public BoardResponse modifyArticle(Integer articleId, BoardRequest boardRequest){
        Board article = boardDtoService.toEntity(boardRequest);

        Board originArticle = (Board) boardRepository.findById(articleId).get();
        originArticle.modifyArticle(boardRequest);
        em.flush();

        return boardDtoService.fromEntity(boardRequest.getMemberId(), originArticle);
    }

    // 글 삭제
    public Integer removeArticle(Integer articleId){

        if(boardRepository.findById(articleId) == null){
            return 0;
        }

        boardRepository.deleteById(articleId);
        return articleId;
    }

    // 자유게시판 글 저장
    public Integer saveFreeBoard(BoardRequest boardRequest){
        Board article = freeBoardRepository.save((FreeBoard) boardDtoService.toEntity(boardRequest));
        return article.getId();
    }

    // 면접후기게시판 글 저장
    public Integer saveInterviewReviewBoard(BoardRequest boardRequest){
        Board article = interviewReviewBoardRepository.save((InterviewReviewBoard) boardDtoService.toEntity(boardRequest));
        return article.getId();
    }

    // 질문게시판 글 저장
    public Integer saveQuestionBoard(BoardRequest boardRequest){
        Board article = questionBoardRepository.save((QuestionBoard) boardDtoService.toEntity(boardRequest));
        return article.getId();
    }

    // 스터디게시판 글 저장
    public Integer saveStudyBoard(StudyBoardRequest studyBoardRequest){
        Board article = studyBoardRepository.save((StudyBoard) boardDtoService.toEntity(studyBoardRequest));
        return article.getId();
    }

}
