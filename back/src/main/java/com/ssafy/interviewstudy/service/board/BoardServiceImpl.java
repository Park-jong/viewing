package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.dto.board.FileResponse;
import com.ssafy.interviewstudy.repository.board.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardDtoManger boardDtoManger;
    private final BoardFileService boardFileService;

    //글 리스트 조회, crud, 검색, 댓글 crud, 글 좋아요, 댓글 좋아요, 글 신고
    // 글 검색
    @Override
    public Page<BoardResponse> findArticleByKeyword(String searchBy, String keyword, BoardType boardType, Pageable pageable) {
        Page<Board> articles = findArticles(searchBy, keyword, boardType, pageable);
        List<BoardResponse> responseList = new ArrayList<>();
        for (Board board : articles.getContent()) {
            responseList.add(boardDtoManger.fromEntityWithoutContent(board));
        }
        return new PageImpl<>(responseList, pageable, articles.getTotalElements());
    }

    private Page<Board> findArticles(String searchBy, String keyword, BoardType boardType, Pageable pageable) {
        if (searchBy.equals("title")) {
            return findArticleByKeywordWithTitle(keyword, boardType, pageable);
        } else if (searchBy.equals("content")) {
            return findArticleByKeywordWithContent(keyword, boardType, pageable);
        } else if (searchBy.equals("author")) {
            return findArticleByKeywordWithAuthor(keyword, boardType, pageable);
        } else {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
    }

    private Page<Board> findArticleByKeywordWithTitle(String keyword, BoardType boardType, Pageable pageable) {
        return boardRepository.findByTitleContaining(keyword, boardType, pageable);
    }

    private Page<Board> findArticleByKeywordWithContent(String keyword, BoardType boardType, Pageable pageable) {
        return boardRepository.findByTitleOrContent(keyword, boardType, pageable);
    }

    private Page<Board> findArticleByKeywordWithAuthor(String keyword, BoardType boardType, Pageable pageable) {
        return boardRepository.findWithAuthor(keyword, boardType, pageable);
    }

    //글 목록 조회
    @Override
    public Page<BoardResponse> findBoardList(BoardType boardType, Pageable pageable) {
        Page<Board> content = boardRepository.findByType(boardType, pageable);
        List<Board> boardList = content.getContent();
        List<BoardResponse> responseList = new ArrayList<>();
        for (Board b : boardList) {
            responseList.add(boardDtoManger.fromEntityWithoutContent(b));
        }
        return new PageImpl<>(responseList, pageable, content.getTotalElements());
    }

    // 글 detail 조회
    @Transactional
    @Override
    public BoardResponse findArticle(Integer memberId, Integer articleId, BoardType boardType) {
        Board article = boardRepository.findById(articleId).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
        List<ArticleFile> files = article.getFiles();
        List<FileResponse> fileResponses = new ArrayList<>();
        modifyViewCount(article);
        for (ArticleFile file : files) {
            fileResponses.add(new FileResponse(file));
        }
        return makeDetailResponse(memberId, article, boardType, fileResponses);
    }

    private BoardResponse makeDetailResponse(Integer memberId, Board article, BoardType boardType, List<FileResponse> files) {
        BoardResponse boardResponse = boardDtoManger.fromEntityToResponse(memberId, article);
        boardResponse.setBoardType(boardType);
        boardResponse.setArticleFiles(files);
        return boardResponse;
    }

    // 글 저장
    @Transactional
    @Override
    public Integer saveBoard(BoardRequest boardRequest, List<MultipartFile> files) {
        Board article = boardRepository.save(boardDtoManger.fromRequestToEntity(boardRequest));
        boardFileService.saveFiles(boardRequest, article, files);
        return article.getId();
    }

    // 글 수정
    @Transactional
    @Override
    public BoardResponse modifyArticle(Integer articleId, BoardRequest boardRequest, List<MultipartFile> files) {
        Board article = boardRepository.findById(articleId).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
        article.modifyArticle(boardRequest);
        boardFileService.saveFiles(boardRequest, article, files);
        return boardDtoManger.fromEntityToResponse(boardRequest.getMemberId(), article);
    }

    // 글 삭제
    @Transactional
    @Override
    public Integer removeArticle(Integer articleId) {
        if (boardRepository.findById(articleId).isEmpty()) {
            return 0;
        }
        boardFileService.removeFiles(articleId);
        boardRepository.deleteById(articleId);
        return articleId;
    }

    // 조회수+1
    @Transactional
    @Override
    public void modifyViewCount(Board article) {
        article.updateViewCount();
        boardRepository.save(article);
    }

    @Override
    public Boolean checkAuthor(Integer articleId, Integer memberId) {
        Board article = boardRepository.findById(articleId).orElseThrow(() -> new EntityNotFoundException("해당 게시글이 존재하지 않습니다."));
        return Objects.equals(article.getAuthor().getId(), memberId);
    }


}
