package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.BoardType;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.StudyRequestFile;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.dto.board.FileResponse;
import com.ssafy.interviewstudy.dto.study.RequestFile;
import com.ssafy.interviewstudy.repository.board.ArticleFileRepository;
import com.ssafy.interviewstudy.repository.board.BoardRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.service.redis.ArticleLikeService;
import com.ssafy.interviewstudy.support.file.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class BoardService {

    private final FileManager fm;
    private final BoardRepository boardRepository;
    private final BoardDtoService boardDtoService;
    private final ArticleFileRepository articleFileRepository;
    private final MemberRepository memberRepository;
    private final ArticleLikeService articleLikeService;

    //글 리스트 조회, crud, 검색, 댓글 crud, 글 좋아요, 댓글 좋아요, 글 신고
    // 글 검색
    public Page<BoardResponse> findArticleByKeyword(String searchBy, String keyword, BoardType boardType, Pageable pageable) {
        Page<Board> articles;
        List<BoardResponse> responseList = new ArrayList<>();
        if (searchBy.equals("title")) {
            articles = findArticleByKeywordWithTitle(keyword, boardType, pageable);
        } else if (searchBy.equals("content")) {
            articles = findArticleByKeywordWithContent(keyword, boardType, pageable);
        } else if (searchBy.equals("author")) {
            articles = findArticleByKeywordWithAuthor(keyword, boardType, pageable);
        } else {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
        for (Board b : articles.getContent()) {
            responseList.add(boardDtoService.fromEntityWithoutContent(b));
        }
        return new PageImpl<>(responseList, pageable, articles.getTotalElements());
    }

    private Page<Board> findArticleByKeywordWithTitle(String keyword, BoardType boardType, Pageable pageable){
        return boardRepository.findByTitleContaining(keyword, boardType, pageable);
    }

    private Page<Board> findArticleByKeywordWithContent(String keyword, BoardType boardType, Pageable pageable){
        return boardRepository.findByTitleOrContent(keyword, boardType, pageable);
    }

    private Page<Board> findArticleByKeywordWithAuthor(String keyword, BoardType boardType, Pageable pageable){
        return boardRepository.findWithAuthor(keyword, boardType, pageable);
    }

    //글 목록 조회
    public Page<BoardResponse> findBoardList(BoardType boardType, Pageable pageable) {
        Page<Board> content = boardRepository.findByType(boardType, pageable);
        List<Board> boardList = content.getContent();
        List<BoardResponse> responseList = new ArrayList<>();
        for (Board b : boardList) {
            responseList.add(boardDtoService.fromEntityWithoutContent(b));
        }
        return new PageImpl<>(responseList, pageable, content.getTotalElements());
    }

    // 글 detail 조회
    @Transactional
    public BoardResponse findArticle(Integer memberId, Integer articleId, BoardType boardType) {
        Board article = boardRepository.findById(articleId).get();
        List<ArticleFile> files = article.getFiles();
        List<FileResponse> fileResponses = new ArrayList<>();

        if (article != null) modifyViewCount(article);

        for (ArticleFile file : files) {
            fileResponses.add(new FileResponse(file));
        }
        // Null이면 예외 발생 처리
        BoardResponse boardResponse = fromEntity(memberId, article);
        boardResponse.setBoardType(boardType);
        boardResponse.setArticleFiles(fileResponses);
        return boardResponse;
    }

    private BoardResponse fromEntity(Integer memberId, Board article) {
        BoardResponse boardResponse = boardDtoService.fromEntityWithoutContent(article);
        if (memberId != null) {
            boardResponse.setIsLike(articleLikeService.checkMemberLikeArticle(article.getId(), memberId));
        }
        boardResponse.setContent(article.getContent());
        boardResponse.setTitle(article.getTitle());
        boardResponse.setCreatedAt(article.getCreatedAt());
        boardResponse.setUpdatedAt(article.getUpdatedAt());
        return boardResponse;
    }

    // 글 수정
    @Transactional
    public BoardResponse modifyArticle(Integer articleId, BoardRequest boardRequest, List<MultipartFile> files) {
        Board originArticle = boardRepository.findById(articleId).get();
        originArticle.modifyArticle(boardRequest);
        boardRepository.save(originArticle);

        // 파일 저장
        if (files != null) {
            for (MultipartFile file : files) {
                try {
                    String saveFileName = boardRequest.getMemberId() + "_" + String.valueOf(System.currentTimeMillis());
                    fm.upload(file.getInputStream(), saveFileName, file.getContentType(), file.getSize());
                    ArticleFile articleFile = new ArticleFile(originArticle, file, saveFileName);
                    articleFileRepository.save(articleFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return fromEntity(boardRequest.getMemberId(), originArticle);
    }


    // 글 삭제
    @Transactional
    public Integer removeArticle(Integer articleId) {
        if (boardRepository.findById(articleId) == null) {
            return 0;
        }
        removeFiles(articleId);
        boardRepository.deleteById(articleId);
        return articleId;
    }

    @Transactional
    public void removeFileList(List<FileResponse> files) {
        for (FileResponse f : files) {
            ArticleFile file = articleFileRepository.findById(f.getFileId()).get();
            fm.delete(file.getSaveFileName());
            articleFileRepository.deleteById(file.getId());
        }
    }

    // 글 저장
    @Transactional
    public Integer saveBoard(BoardRequest boardRequest, List<MultipartFile> files) {
        Board article = boardRepository.save(toEntity(boardRequest));
        // 파일 저장
        if (files != null) {
            for (MultipartFile file : files) {
                try {
                    String saveFileName = boardRequest.getMemberId() + "_" + String.valueOf(System.currentTimeMillis());
                    fm.upload(file.getInputStream(), saveFileName, file.getContentType(), file.getSize());
                    ArticleFile articleFile = new ArticleFile(article, file, saveFileName);
                    articleFileRepository.save(articleFile);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return article.getId();
    }

    private Board toEntity(BoardRequest boardRequest) {
        Member author = memberRepository.findMemberById(boardRequest.getMemberId());
        Board board = Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .author(author)
                .boardType(boardRequest.getBoardType())
                .build();
        return board;
    }

    // 파일 삭제
    @Transactional
    public void removeFiles(Integer articleId) {
        List<ArticleFile> files = articleFileRepository.findByArticle_Id(articleId);

        for (ArticleFile file : files) {
            fm.delete(file.getSaveFileName());
            articleFileRepository.deleteById(file.getId());
        }
    }

    // 파일 다운로드
    public FileResponse fileDownload(Integer fileId) {
        ArticleFile articleFile = articleFileRepository.findById(fileId).get();
        byte[] file = null;
        FileResponse result = new FileResponse(articleFile);

        try {
            file = fm.download(articleFile.getSaveFileName());
            result.setFileData(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    // 조회수+1
    @Transactional
    public void modifyViewCount(Board article) {
        article.updateViewCount();
        boardRepository.save(article);
    }

    public Boolean checkAuthor(Integer articleId, Integer memberId) {
        Board article = boardRepository.findById(articleId).get();
        return Objects.equals(article.getAuthor().getId(), memberId);
    }

}
