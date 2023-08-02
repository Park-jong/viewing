package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.domain.study.Study;
import com.ssafy.interviewstudy.dto.board.*;
import com.ssafy.interviewstudy.repository.board.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.ArticleLikeRepository;
import com.ssafy.interviewstudy.repository.board.StudyBoardCommentRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import com.ssafy.interviewstudy.repository.study.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyBoardDtoService {
    private final MemberRepository memberRepository;
    private final StudyRepository studyRepository;
    private final StudyBoardCommentRepository studyBoardCommentRepository;


    public StudyBoard toEntity(BoardRequest boardRequest) {
        Member author = memberRepository.findMemberById(boardRequest.getMemberId());
        Study study = studyRepository.findById(boardRequest.getStudyId()).get();

        System.out.println(study.getId());
        StudyBoard article = StudyBoard.builder()
                .study(study)
                .title(boardRequest.getTitle())
                .content(boardRequest.getContent())
                .author(author)
                .files(boardRequest.getFiles())
                .build();

        return article;
    }

    public StudyBoardResponse fromEntityWithoutContent(StudyBoard article) {
        StudyBoardResponse boardResponse = StudyBoardResponse.builder()
                .id(article.getId())
                .studyId(article.getStudy().getId())
                .author(new Author(article.getAuthor()))
                .title(article.getTitle())
                .commentCount(studyBoardCommentRepository.countByArticle(article))
                .build();

        return boardResponse;
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
