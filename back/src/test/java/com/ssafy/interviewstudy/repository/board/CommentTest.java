package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.InterviewStudyApplication;
import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.FreeBoard;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ContextConfiguration(classes = InterviewStudyApplication.class)
@SpringBootTest
public class CommentTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    FreeBoardRepository freeBoardRepository;

    @Autowired
    ArticleCommentRepository articleCommentRepository;

    @Autowired
    CommentLikeRepository commentLikeRepository;

    @Autowired
    MemberRepository memberRepository;

    //댓글 insert
//    @Rollback(value = false)
    @Test
    void commentTest(){
        String title = "제목입니다.";
        String content = "내용내용내용";

        Member member = memberRepository.save(Member.builder()
                .email("khk@gmail.com")
                .nickname("kk").build());

        Board article = freeBoardRepository.save(FreeBoard.builder()
                .title(title)
                .content(content)
                .author(member)
                .build());

        articleCommentRepository.save(ArticleComment.builder()
                .content("댓글입니다")
                .author(member)
                .article(article)
                .isDelete(false)
                .build());

        List<ArticleComment> comments = articleCommentRepository.findAll();

        ArticleComment comment = comments.get(0);
        assertThat(comment.getContent()).isEqualTo("댓글입니다");
    }


}
