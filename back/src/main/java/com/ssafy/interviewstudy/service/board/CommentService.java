package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.CommentLike;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.dto.board.CommentRequest;
import com.ssafy.interviewstudy.dto.board.CommentResponse;
import com.ssafy.interviewstudy.repository.board.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.BoardRepository;
import com.ssafy.interviewstudy.repository.board.CommentLikeRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private BoardRepository boardRepository;
    private ArticleCommentRepository articleCommentRepository;
    private CommentLikeRepository commentLikeRepository;
    private BoardDtoService boardDtoService;
    private MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    public CommentService(BoardRepository boardRepository, ArticleCommentRepository articleCommentRepository, CommentLikeRepository commentLikeRepository, BoardDtoService boardDtoService) {
        this.boardRepository = boardRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.boardDtoService = boardDtoService;
    }

    // 게시글 댓글 저장
    public Integer saveComment(CommentRequest commentRequest){

        ArticleComment comment = articleCommentRepository.save(boardDtoService.toEntity(commentRequest));

        return comment.getId();
    }

    // 게시글 댓글 조회
    public List<CommentResponse> findComments(Integer memberId, Integer articleId){
        List<ArticleComment> comment = articleCommentRepository.findAllByArticle((Board) boardRepository.findById(articleId).get());
        List<CommentResponse> commentResponses = new ArrayList<>();

        for (ArticleComment c: comment) {
            commentResponses.add(boardDtoService.fromEntity(memberId, c));
        }

        return commentResponses;
    }

    // 대댓글 조회
    public List<CommentResponse> findCommentReplies(Integer memberId, Integer articleId, Integer commentId){
        List<ArticleComment> replies = articleCommentRepository.findRepliesByComment(commentId);
        List<CommentResponse> commentResponses = new ArrayList<>();

        for (ArticleComment c: replies) {
            commentResponses.add(boardDtoService.fromEntityWithoutCommentCount(memberId, c));
        }

        return commentResponses;
    }

    // (대)댓글 수정
    public CommentResponse modifyComment(Integer commentId, CommentRequest commentRequest){

        ArticleComment originComment = articleCommentRepository.findById(commentId).get();

        originComment.modifyComment(commentRequest);
        em.flush();

        return boardDtoService.fromEntity(commentRequest.getMemberId(), originComment);
    }

    // (대)댓글 삭제
    public void removeComment(Integer commentId){
        ArticleComment comment = articleCommentRepository.findById(commentId).get();
        comment.deleteComment();
    }

    // 댓글 좋아요
    public Integer saveCommentLike(Integer memberId, Integer commentId){

        // 이미 좋아요를 누른 회원인지 체크
        if(commentLikeRepository.existsByMember_IdAndComment_Id(memberId, commentId))
            return 0;

        Member member = memberRepository.findMemberById(memberId);
        ArticleComment comment = articleCommentRepository.findById(commentId).get();

        CommentLike commentLike =commentLikeRepository.save(CommentLike.builder()
                .member(member)
                .comment(comment)
                .build());
        return commentLike.getId();
    }

    // 댓글 좋아요 삭제
    public void removeCommentLike(Integer memberId, Integer commentId){
        commentLikeRepository.removeCommentLikeByCommentAndMember(articleCommentRepository.findById(commentId).get()
                ,memberRepository.findMemberById(memberId));
    }
}
