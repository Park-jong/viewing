package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.FileResponse;
import com.ssafy.interviewstudy.dto.board.StudyBoardResponse;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.dto.notification.NotificationStudyDto;
import com.ssafy.interviewstudy.repository.board.ArticleFileRepository;
import com.ssafy.interviewstudy.repository.board.StudyBoardRepository;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import com.ssafy.interviewstudy.support.file.FileManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class StudyBoardService {

    private final ArticleFileRepository articleFileRepository;
    private final StudyBoardRepository boardRepository;
    private final StudyBoardDtoService boardDtoService;
    private final NotificationService notificationService;

    private FileManager fm = FileManager.getInstance();

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
    @Transactional
    public StudyBoardResponse modifyArticle(Integer articleId, BoardRequest boardRequest, List<MultipartFile> files){
        StudyBoard originArticle = boardRepository.findById(articleId).get();
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

        return boardDtoService.fromEntity(originArticle);
    }

    // 글 삭제
    @Transactional
    public Integer removeArticle(Integer articleId){
        if(boardRepository.findById(articleId) == null){
            return 0;
        }

        boardRepository.deleteById(articleId);
        return articleId;
    }

    // 글 저장
    public Integer saveBoard(BoardRequest boardRequest, List<MultipartFile> files){
        StudyBoard article = boardRepository.save(boardDtoService.toEntity(boardRequest));

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

        //스터디 게시판에 게시글이 달리면 스터디원들에게 알림이 가야함
        if(article.getId()!=null){
            notificationService
                    .sendNotificationToStudyMember(
                            NotificationStudyDto
                                    .builder()
                                    .notificationDto(
                                            NotificationDto
                                                    .builder()
                                                    .memberId(boardRequest.getMemberId())
                                                    .content(article.getStudy().getTitle()+" 스터디에 게시글이 작성되었습니다.")
                                                    .url(article.getStudy().getId().toString()+" "+article.getId())
                                                    .notificationType(NotificationType.StudyArticle)
                                                    .build()
                                    )
                                    .studyId(article.getStudy().getId())
                                    .build()
                    );
        }
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

    // 파일 삭제
    @Transactional
    public void removeFiles(Integer articleId){
        articleFileRepository.removeByStudyArticleId(articleId);
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

}
