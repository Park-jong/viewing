package com.ssafy.interviewstudy.service.member;

import com.ssafy.interviewstudy.domain.board.*;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.Author;
import com.ssafy.interviewstudy.dto.board.BoardRequest;
import com.ssafy.interviewstudy.dto.board.BoardResponse;
import com.ssafy.interviewstudy.repository.board.*;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class MemberCommentServiceTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    private MemberCommentService memberCommentService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private FreeBoardRepository freeBoardRepository;

    @Autowired
    private QuestionBoardRepository questionBoardRepository;

    @Autowired
    private InterviewReviewBoardRepository interviewReviewBoardRepository;

    @Autowired
    private ArticleCommentRepository articleCommentRepository;

    @Test
    @DisplayName("댓글 단 게시글 조회 테스트")
    public void retrieveCommentedArticleTest(){
        //give
        //게시글 작성할 멤버 생성
        Member memberA = Member.builder().email("tkdwo@gmail.com").nickname("ssafy").build();

        //댓글 작성할 멤버 생성
        Member memberB = Member.builder().email("sj_1333@naver.com").nickname("ssafy123").build();

        memberRepository.save(memberA);
        memberRepository.save(memberB);

        //게시글 작성
        //자유 게시판
        FreeBoard freeBoardA = new FreeBoard();
        freeBoardA.setTitle("ssafy");
        freeBoardA.setContent("내용내용");
        freeBoardA.setAuthor(memberA);
        freeBoardA.setViewCount(0);
        freeBoardRepository.save(freeBoardA);

        FreeBoard freeBoardB = new FreeBoard();
        freeBoardB.setTitle("ssafy2");
        freeBoardB.setContent("내용내용2");
        freeBoardB.setAuthor(memberA);
        freeBoardB.setViewCount(0);
        freeBoardRepository.save(freeBoardB);

        //질문 게시판
        InterviewReviewBoard interviewReviewBoardA = new InterviewReviewBoard();
        interviewReviewBoardA.setTitle("ssafy");
        interviewReviewBoardA.setContent("내용내용");
        interviewReviewBoardA.setAuthor(memberA);
        interviewReviewBoardA.setViewCount(0);
        interviewReviewBoardRepository.save(interviewReviewBoardA);

        InterviewReviewBoard interviewReviewBoardB = new InterviewReviewBoard();
        interviewReviewBoardB.setTitle("ssafy2");
        interviewReviewBoardB.setContent("내용내용2");
        interviewReviewBoardB.setAuthor(memberA);
        interviewReviewBoardB.setViewCount(0);
        interviewReviewBoardRepository.save(interviewReviewBoardB);

        //댓글 작성
        ArticleComment articleCommentA =
                ArticleComment.builder().author(memberB).content("하하").article(freeBoardA).isDelete(false).build();

        ArticleComment articleCommentB =
                ArticleComment.builder().author(memberB).content("히히").article(freeBoardB).isDelete(false).build();

        ArticleComment articleCommentC =
                ArticleComment.builder().author(memberA).content("히히").article(interviewReviewBoardA).isDelete(false).build();

        ArticleComment articleCommentD =
                ArticleComment.builder().author(memberA).content("히히").article(interviewReviewBoardB).isDelete(false).build();

        articleCommentRepository.save(articleCommentA);
        articleCommentRepository.save(articleCommentB);
        articleCommentRepository.save(articleCommentC);
        articleCommentRepository.save(articleCommentD);

        //when

        //memberB가 질문게시판에 작성한 댓글을 가져옴
        BoardRequest boardRequest = new BoardRequest();
        boardRequest.setMemberId(memberB.getId());
        List<BoardResponse> boardList = memberCommentService.getCommentedArticle(boardRequest, BoardType.FreeBoard);

        //memberA가 면접후기 게시판에 작성한 댓글을 가져옴
        BoardRequest boardRequest2 = new BoardRequest();
        boardRequest2.setMemberId(memberA.getId());
        List<BoardResponse> boardList2 = memberCommentService.getCommentedArticle(boardRequest2, BoardType.InterviewReviewBoard);

        //then
        //memberB가 작성한 댓글에 대한 검증
        assertThat(boardList)
                .extracting("id")
                .contains(freeBoardA.getId(),freeBoardB.getId())
                .hasSize(2); 
        
        //memberA가 작성한 댓글에 대한 검증
        assertThat(boardList2)
                .extracting("id")
                .contains(interviewReviewBoardA.getId(),interviewReviewBoardB.getId())
                .hasSize(2);

    }

}
