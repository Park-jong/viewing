package com.ssafy.interviewstudy.service.board.studyBoard;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.notification.NotificationType;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.FileResponse;
import com.ssafy.interviewstudy.dto.board.StudyBoardResponse;
import com.ssafy.interviewstudy.dto.notification.NotificationDto;
import com.ssafy.interviewstudy.exception.board.BoardExceptionFactory;
import com.ssafy.interviewstudy.repository.board.studyBoard.StudyBoardRepository;
import com.ssafy.interviewstudy.service.board.generalBoard.BoardFileService;
import com.ssafy.interviewstudy.service.notification.NotificationDtoManager;
import com.ssafy.interviewstudy.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class StudyBoardServiceImpl implements StudyBoardService {

    private final StudyBoardRepository boardRepository;
    private final StudyBoardDtoManager boardDtoManager;
    private final NotificationService notificationService;
    private final NotificationDtoManager notificationDtoManager;
    private final BoardFileService boardFileService;

    //글 리스트 조회, crud, 검색, 댓글 crud, 글 좋아요, 댓글 좋아요, 글 신고
    //글 목록 조회
    @Override
    public Page<StudyBoardResponse> findBoardList(Integer studyId, Pageable pageable) {
        Page<StudyBoard> content = boardRepository.findByStudyId(studyId, pageable);
        List<StudyBoard> boardList = content.getContent();
        List<StudyBoardResponse> responseList = boardList.stream().map(boardDtoManager::fromEntityWithoutContent).collect(Collectors.toList());
        return new PageImpl<>(responseList, pageable, content.getTotalElements());
    }

    // 글 detail 조회
    @Override
    public StudyBoardResponse findArticle(Integer articleId) {
        StudyBoard article = boardRepository.findById(articleId).orElseThrow(BoardExceptionFactory::articleNotFound);
        List<ArticleFile> files = article.getFiles();
        List<FileResponse> fileResponses = Optional.ofNullable(files).orElse(Collections.emptyList()).stream().map(FileResponse::new).collect(Collectors.toList());;
        StudyBoardResponse boardResponse = boardDtoManager.fromEntity(article);
        boardResponse.setArticleFiles(fileResponses);
        return boardResponse;
    }

    // 글 수정
    @Transactional
    @Override
    public StudyBoardResponse modifyArticle(Integer articleId, BoardRequest boardRequest, List<MultipartFile> files) {
        StudyBoard originArticle = boardRepository.findById(articleId).orElseThrow(BoardExceptionFactory::articleNotFound);
        originArticle.modifyArticle(boardRequest);
        boardRepository.save(originArticle);
        boardFileService.saveFiles(boardRequest, originArticle, files);
        return boardDtoManager.fromEntity(originArticle);
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

    // 글 저장
    @Transactional
    @Override
    public Integer saveBoard(BoardRequest boardRequest, List<MultipartFile> files) {
        StudyBoard article = boardRepository.save(boardDtoManager.toEntity(boardRequest));
        boardFileService.saveFiles(boardRequest, article, files);
        sendNotificationAboutNewArticle(boardRequest, article);
        return article.getId();
    }

    private void sendNotificationAboutNewArticle(BoardRequest boardRequest, StudyBoard article) {
        int receiver = boardRequest.getMemberId();
        String content = article.getStudy().getTitle() + " 스터디에 게시글이 작성되었습니다.";
        NotificationType type = NotificationType.StudyArticle;
        String url = article.getStudy().getId().toString() + " " + article.getId().toString();
        boolean isRead = false;
        NotificationDto notificationDto = notificationDtoManager.makeNotification(receiver, content, type, url, isRead);
        notificationService.sendNotificationToMember(notificationDto);
    }

    // 글 검색
    @Override
    public Page<StudyBoardResponse> findArticleByKeyword(Integer studyId, String searchBy, String keyword, Pageable pageable) {
        Page<StudyBoard> articles;
        if (searchBy.equals("title")) {
            articles = boardRepository.findByTitleContaining(studyId, keyword, pageable);
        } else if (searchBy.equals("content")) {
            articles = boardRepository.findByTitleOrContent(studyId, keyword, pageable);
        } else {
            articles = boardRepository.findWithAuthor(studyId, keyword, pageable);
        }
        List<StudyBoardResponse> responseList = articles.stream().map(boardDtoManager::fromEntityWithoutContent).collect(Collectors.toList());
        return new PageImpl<>(responseList, pageable, articles.getTotalElements());
    }

    // 글 작성자가 본인인지 아닌지 체크
    @Override
    public Boolean checkAuthor(Integer articleId, Integer memberId) {
        StudyBoard article = boardRepository.findById(articleId).orElseThrow(BoardExceptionFactory::articleNotFound);
        return article.getAuthor().getId().equals(memberId);
    }

}
