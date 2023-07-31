package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.*;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.dto.board.StudyBoardRequest;
import com.ssafy.interviewstudy.repository.board.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BoardService {

    final int pageSize = 20;

    private BoardRepository boardRepository;
    private StudyBoardRepository studyBoardRepository;
    private BoardDtoService boardDtoService;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    public BoardService(BoardRepository boardRepository, StudyBoardRepository studyBoardRepository, BoardDtoService boardDtoService) {
        this.boardRepository = boardRepository;
        this.studyBoardRepository = studyBoardRepository;
        this.boardDtoService = boardDtoService;
    }

    //글 리스트 조회, crud, 검색, 댓글 crud, 글 좋아요, 댓글 좋아요, 글 신고

    //글 목록 조회
    public List<BoardResponse> findBoardList(Integer memberId, BoardType boardType, int page) {
        List<Board> boardList = boardRepository.findByType(boardType, PageRequest.of(page, pageSize)).getContent();
        List<BoardResponse> responseList = new ArrayList<>();

        for (Board b : boardList) {
            responseList.add(boardDtoService.fromEntityWithoutContent(b));
        }

        return responseList;
    }

    // 글 detail 조회
    public BoardResponse findArticle(Integer memberId, Integer articleId, BoardType boardType) {
        Optional<Board> article = boardRepository.findById(articleId);
        // 페이지도 나중에 request 받기

        // Null이면 예외 발생 처리
        BoardResponse boardResponse = boardDtoService.fromEntity(memberId, article.orElseThrow(NullPointerException::new));
        boardResponse.setBoardType(boardType);

        return boardResponse;
    }

    // 글 수정
    public BoardResponse modifyArticle(Integer articleId, BoardRequest boardRequest){
        Board originArticle = boardRepository.findById(articleId).get();
        originArticle.modifyArticle(boardRequest);
        boardRepository.save(originArticle);

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

    // 글 저장
    public Integer saveFreeBoard(BoardRequest boardRequest){
        Board article = boardRepository.save(boardDtoService.toEntity(boardRequest));
        return article.getId();
    }

    // 글 검색
    public List<BoardResponse> findByTitle(String searchBy, String keyword, BoardType boardType, Pageable pageable){
        List<Board> articles;
        List<BoardResponse> responseList = new ArrayList<>();
        if(searchBy.equals("title")) articles = boardRepository.findByTitleContaining(keyword, boardType, pageable).getContent();
        else if(searchBy.equals("content")) articles = boardRepository.findByTitleOrContent(keyword, boardType, pageable).getContent();
        else articles = boardRepository.findWithAuthor(keyword, boardType, pageable).getContent();

        for (Board b: articles) {
            responseList.add(boardDtoService.fromEntityWithoutContent(b));
        }

        return responseList;
    }

    // 조회수+1
    public void modifyViewCount(Integer articleId){
        Board article = boardRepository.findById(articleId).get();
        article.updateViewCount();
        boardRepository.save(article);
    }


    // 글 작성자가 본인인지 아닌지 체크
    public Boolean checkAuthor(Integer articleId, Integer memberId){
        Board article = boardRepository.findById(articleId).get();

        // 본인이면 true, 아니면 false
        if(article.getAuthor().getId() == memberId) return true;
        else return false;
    }


}
