package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.InterviewStudyApplication;
import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.ArticleLike;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.FreeBoard;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = InterviewStudyApplication.class)
@SpringBootTest
public class BoardTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    ArticleLikeRepository articleLikeRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    FreeBoardRepository freeBoardRepository;

    @Autowired
    InterviewReviewBoardRepository interviewReviewBoardRepository;

    @Autowired
    NoticeBoardRepository noticeBoardRepository;

    @Autowired
    QuestionBoardRepository questionBoardRepository;

    @Autowired
    StudyBoardRepository studyBoardRepository;

    @Autowired
    MemberRepository memberRepository;

    @Test
    void saveTest(){
        String title = "제목입니다.";
        String content = "왜 조회수가 맘대로 안될까";
        freeBoardRepository.save(FreeBoard.builder()
                        .title(title)
                        .content(content)
                        .author(Member.builder().email("khk@gmail.com").nickname("kk").build())
                .build());

        List<FreeBoard> freeBoards = boardRepository.findAll();

        FreeBoard freeBoard = freeBoards.get(0);
        assertThat(freeBoard.getTitle()).isEqualTo(title);
        assertThat(freeBoard.getContent()).isEqualTo(content);

    }

    @Test
    void updateViewCountTest(){
        String title = "제목입니다.";
        String content = "왜 조회수가 맘대로 안될까";
        freeBoardRepository.save(FreeBoard.builder()
                .title(title)
                .content(content)
                .author(Member.builder().email("khk@gmail.com").nickname("kk").build())
                .build());

        boardRepository.updateViewCount(1);
        List<Board> freeBoards = boardRepository.findAll();


        Board freeBoard = freeBoards.get(0);
        assertThat(freeBoard.getViewCount()).isEqualTo(1);
    }

    @Test
    void searchByMemberIdTest(){
        String title = "제목입니다.";
        String content = "왜 조회수가 맘대로 안될까222";
        freeBoardRepository.save(FreeBoard.builder()
                .title(title)
                .content(content)
                .author(Member.builder().email("khk@gmail.com").nickname("kk").build())
                .build());

        List<Board> article = boardRepository.findByMemberId(1, PageRequest.of(0, 1)).getContent();
        assertThat(article.get(0).getTitle()).isEqualTo(title);

    }

    @Test
    void searchTest(){
        String title = "제목입니다.";
        String content = "내용내용내용";
        freeBoardRepository.save(FreeBoard.builder()
                .title(title)
                .content(content)
                .author(Member.builder().email("khk@gmail.com").nickname("kk").build())
                .build());

        List<FreeBoard> article1 = freeBoardRepository.findByTitleContaining("제목", PageRequest.of(0, 1)).getContent();
        assertThat(article1.get(0).getTitle()).isEqualTo(title);
        List<FreeBoard> article2 = freeBoardRepository. findByTitleOrContent("용내", PageRequest.of(0, 1)).getContent();
        assertThat(article2.get(0).getTitle()).isEqualTo(title);
        List<FreeBoard> article3 = freeBoardRepository.findWithAuthor("kk", PageRequest.of(0, 1)).getContent();
        assertThat(article3.get(0).getTitle()).isEqualTo(title);

    }

    @Test
    void likeTest(){
        String title = "제목입니다.";
        String content = "내용내용내용";
        Member member = Member.builder().email("khk@gmail.com").nickname("kk").build();

        Board article = freeBoardRepository.save(FreeBoard.builder()
                .title(title)
                .content(content)
                .author(member)
                .build());

        articleLikeRepository.save(ArticleLike.builder()
                        .article(article)
                        .member(member)
                .build());

        Boolean isExist = articleLikeRepository.existsByMemberAndArticle(member, article);

        assertThat(isExist).isEqualTo(true);
    }

}
