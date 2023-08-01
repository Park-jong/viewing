package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.*;
import com.ssafy.interviewstudy.repository.board.*;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardDtoService {
    private final ArticleCommentRepository commentRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StudyBoardCommentRepository studyBoardCommentRepository;


    public Board toEntity(BoardRequest boardRequest) {
        Member author = memberRepository.findMemberById(boardRequest.getMemberId());

        Board article = Board.builder()
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .author(author)
                .boardType(boardRequest.getBoardType())
                .files(boardRequest.getFiles())
                .build();

        return article;
    }

    public BoardResponse fromEntityWithoutContent(Board article) {
        BoardResponse boardResponse = BoardResponse.builder()
                .id(article.getId())
                .author(new Author(article.getAuthor()))
                .title(article.getTitle())
                .viewCount(article.getViewCount())
                .commentCount(commentRepository.countByArticle(article))
                .likeCount(articleLikeRepository.countByArticle(article))
                .build();

        return boardResponse;
    }

    public BoardResponse fromEntity(Integer memberId, Board article) {
        BoardResponse boardResponse = fromEntityWithoutContent(article);

        if(memberId != null) boardResponse.setIsLike(articleLikeRepository.existsByMember_IdAndId(memberId, article.getId()));
        boardResponse.setContent(article.getContent());
        boardResponse.setCreatedAt(article.getCreatedAt());
        boardResponse.setUpdatedAt(article.getUpdatedAt());
        boardResponse.setArticleFiles(article.getFiles());

        return boardResponse;
    }

    public StudyBoard toEntity(StudyBoardRequest studyBoardRequest) {
        Member author = memberRepository.findMemberById(studyBoardRequest.getMemberId());

        StudyBoard article = StudyBoard.builder()
                .title(studyBoardRequest.getTitle())
                .content(studyBoardRequest.getContent())
                .study(studyRepository.findStudyById(studyBoardRequest.getStudyId()))
                .author(author)
                .files(studyBoardRequest.getFiles())
                .build();

        return article;
    }

    public StudyBoardResponse fromEntityWithoutContent(StudyBoard article) {
        StudyBoardResponse studyBoardResponse = StudyBoardResponse.builder()
                .id(article.getId())
                .author(new Author(article.getAuthor()))
                .title(article.getTitle())
                .viewCount(article.getViewCount())
                .commentCount(studyBoardCommentRepository.countByArticle(article))
                .build();

        return studyBoardResponse;
    }

    public StudyBoardResponse fromEntity(StudyBoard article) {
        StudyBoardResponse boardResponse = fromEntityWithoutContent(article);
        boardResponse.setContent(article.getContent());
        boardResponse.setCreatedAt(article.getCreatedAt());
        boardResponse.setUpdatedAt(article.getUpdatedAt());
        boardResponse.setArticleFiles(article.getFiles());

        return boardResponse;
    }


}
