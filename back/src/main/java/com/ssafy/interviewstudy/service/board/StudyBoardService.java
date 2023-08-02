package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.StudyBoardResponse;
import com.ssafy.interviewstudy.repository.board.StudyBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StudyBoardService {

    private final StudyBoardRepository boardRepository;
    private final StudyBoardDtoService boardDtoService;

    //글 리스트 조회, crud, 검색, 댓글 crud, 글 좋아요, 댓글 좋아요, 글 신고

    //글 목록 조회
    public List<StudyBoardResponse> findBoardList(Integer studyId, Pageable pageable) {
        List<StudyBoard> boardList = boardRepository.findByStudy_Id(studyId, pageable).getContent();
        List<StudyBoardResponse> responseList = new ArrayList<>();

        for (StudyBoard b : boardList) {
            responseList.add(boardDtoService.fromEntityWithoutContent(b));
        }

        return responseList;
    }

    // 글 detail 조회
    public StudyBoardResponse findArticle(Integer articleId) {
        StudyBoard article = boardRepository.findById(articleId).get();

        StudyBoardResponse boardResponse = boardDtoService.fromEntity(article);

        return boardResponse;
    }

    // 글 수정
    public StudyBoardResponse modifyArticle(Integer articleId, BoardRequest boardRequest){
        StudyBoard originArticle = boardRepository.findById(articleId).get();
        originArticle.modifyArticle(boardRequest);
        boardRepository.save(originArticle);

        return boardDtoService.fromEntity(originArticle);
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
    public Integer saveBoard(BoardRequest boardRequest){
        StudyBoard article = boardRepository.save(boardDtoService.toEntity(boardRequest));
        return article.getId();
    }

    // 글 검색
    public List<StudyBoardResponse> findArticleByKeyword(Integer studyId, String searchBy, String keyword, Pageable pageable){
        List<StudyBoard> articles;
        List<StudyBoardResponse> responseList = new ArrayList<>();
        if(searchBy.equals("title")) articles = boardRepository.findByTitleContaining(studyId, keyword, pageable).getContent();
        else if(searchBy.equals("content")) articles = boardRepository.findByTitleOrContent(studyId, keyword, pageable).getContent();
        else articles = boardRepository.findWithAuthor(studyId, keyword, pageable).getContent();

        for (StudyBoard b: articles) {
            responseList.add(boardDtoService.fromEntityWithoutContent(b));
        }

        return responseList;
    }

    // 글 작성자가 본인인지 아닌지 체크
    public Boolean checkAuthor(Integer articleId, Integer memberId){
        StudyBoard article = boardRepository.findById(articleId).get();

        // 본인이면 true, 아니면 false
        if(article.getAuthor().getId() == memberId) return true;
        else return false;
    }

}
