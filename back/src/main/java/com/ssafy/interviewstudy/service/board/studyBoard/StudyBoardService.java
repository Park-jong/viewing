package com.ssafy.interviewstudy.service.board.studyBoard;

import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.FileResponse;
import com.ssafy.interviewstudy.dto.board.StudyBoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StudyBoardService {
    //글 목록 조회
    Page<StudyBoardResponse> findBoardList(Integer studyId, Pageable pageable);

    // 글 detail 조회
    StudyBoardResponse findArticle(Integer articleId);

    // 글 수정
    @Transactional
    StudyBoardResponse modifyArticle(Integer articleId, BoardRequest boardRequest, List<MultipartFile> files);

    // 글 삭제
    @Transactional
    Integer removeArticle(Integer articleId);

    // 글 저장
    @Transactional
    Integer saveBoard(BoardRequest boardRequest, List<MultipartFile> files);

    // 글 검색
    Page<StudyBoardResponse> findArticleByKeyword(Integer studyId, String searchBy, String keyword, Pageable pageable);

    // 글 작성자가 본인인지 아닌지 체크
    Boolean checkAuthor(Integer articleId, Integer memberId);
}
