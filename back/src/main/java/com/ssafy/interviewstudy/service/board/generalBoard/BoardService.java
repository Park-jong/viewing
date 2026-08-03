package com.ssafy.interviewstudy.service.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BoardService {
    Page<BoardResponse> findArticleByKeyword(String searchBy, String keyword, BoardType boardType, Pageable pageable);

    Page<BoardResponse> findBoardList(BoardType boardType, Pageable pageable);

    BoardResponse findArticle(Integer memberId, Integer articleId, BoardType boardType);

    Integer saveArticle(BoardRequest boardRequest, List<MultipartFile> files);

    BoardResponse modifyArticle(Integer articleId, BoardRequest boardRequest, List<MultipartFile> files);

    Integer removeArticle(Integer articleId);

    Boolean checkAuthor(Integer articleId, Integer memberId);
}
