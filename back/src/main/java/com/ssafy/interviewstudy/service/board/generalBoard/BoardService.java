package com.ssafy.interviewstudy.service.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    //글 리스트 조회, crud, 검색, 댓글 crud, 글 좋아요, 댓글 좋아요, 글 신고
    // 글 검색
    Page<BoardResponse> findArticleByKeyword(String searchBy, String keyword, BoardType boardType, Pageable pageable);

    //글 목록 조회
    Page<BoardResponse> findBoardList(BoardType boardType, Pageable pageable);

    // 글 detail 조회
    BoardResponse findArticle(Integer memberId, Integer articleId, BoardType boardType);

    // 글 저장
    Integer saveArticle(BoardRequest boardRequest, List<MultipartFile> files);

    // 글 수정
    BoardResponse modifyArticle(Integer articleId, BoardRequest boardRequest, List<MultipartFile> files);

    // 글 삭제
    Integer removeArticle(Integer articleId);

    // 조회수+1
    void incrementAndSaveViewCount(Board article);

    Boolean checkAuthor(Integer articleId, Integer memberId);
}
