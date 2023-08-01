package com.ssafy.interviewstudy.service.board;

import com.ssafy.interviewstudy.annotation.JWTRequired;
import com.ssafy.interviewstudy.domain.board.ArticleComment;
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
    private MemberRepository memberRepository;
    private CommentDtoService commentDtoService;

    @PersistenceContext
    EntityManager em;

    @Autowired
    public CommentService(BoardRepository boardRepository, ArticleCommentRepository articleCommentRepository, CommentLikeRepository commentLikeRepository
            , CommentDtoService commentDtoService, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.articleCommentRepository = articleCommentRepository;
        this.commentLikeRepository = commentLikeRepository;
        this.memberRepository = memberRepository;
        this.commentDtoService = commentDtoService;
    }

    // 게시글 댓글 저장
    @JWTRequired
    public Integer saveComment(Integer articleId, CommentRequest commentRequest){
        commentRequest.setArticleId(articleId);
        ArticleComment comment = articleCommentRepository.save(commentDtoService.toEntity(commentRequest));

        return comment.getId();
    }

    // 대댓글 저장
    public Integer saveCommentReply(Integer articleId, Integer commentId, CommentRequest commentRequest){
        commentRequest.setArticleId(articleId);
        ArticleComment comment = commentDtoService.toEntityWithParent(commentId, commentRequest);

        return articleCommentRepository.save(comment).getId();
    }
//    public

    // 게시글 댓글 조회
    public List<CommentResponse> findComments(Integer memberId, Integer articleId){
        List<ArticleComment> comment = articleCommentRepository.findAllByArticle(boardRepository.findById(articleId).get());
        System.out.println(comment.size());
        findReplies(comment);
        List<CommentResponse> commentResponses = new ArrayList<>();

        for (ArticleComment c: comment) {
            commentResponses.add(commentDtoService.fromEntity(memberId, c));
        }

        return commentResponses;
    }

    public void findReplies(List<ArticleComment> parents){
        for (ArticleComment comment: parents) {
            List<ArticleComment> replies = comment.getReplies();
        }
    }

    // (대)댓글 수정
    public CommentResponse modifyComment(Integer commentId, CommentRequest commentRequest){

        ArticleComment originComment = articleCommentRepository.findById(commentId).get();

        originComment.modifyComment(commentRequest);
        ArticleComment modifiedComment = articleCommentRepository.save(originComment);

        return commentDtoService.fromEntity(commentRequest.getMemberId(), modifiedComment);
    }

    // (대)댓글 삭제
    public void removeComment(Integer commentId){
        ArticleComment comment = articleCommentRepository.findById(commentId).get();
        comment.deleteComment();
        articleCommentRepository.save(comment);
    }

    // 댓글 좋아요
    public Integer saveCommentLike(Integer memberId, Integer commentId){
        Member member = memberRepository.findMemberById(memberId);
        ArticleComment comment = articleCommentRepository.findById(commentId).get();

        // 이미 좋아요를 누른 회원인지 체크
        if(checkMemberLikeComment(memberId, commentId))
            return 0;

        CommentLike commentLike =commentLikeRepository.save(CommentLike.builder()
                .member(member)
                .comment(comment)
                .build());
        return commentLike.getId();
    }

    // 댓글 좋아요 삭제
    public void removeCommentLike(Integer memberId, Integer commentId){
        Member member = memberRepository.findMemberById(memberId);
        ArticleComment comment = articleCommentRepository.findById(commentId).get();

        if(!checkMemberLikeComment(memberId, commentId))
            commentLikeRepository.removeCommentLikeByCommentAndMember(comment, member);
    }

    public Boolean checkMemberLikeComment(Integer memberId, Integer commentId){
        return commentLikeRepository.existsByMember_IdAndComment_Id(memberId, commentId);
    }

    // 댓글 작성자가 본인인지 체크
    public Boolean checkAuthor(Integer commentId, Integer memberId){
        ArticleComment comment = articleCommentRepository.findById(commentId).get();

        if(comment.getAuthor().getId() == memberId) return true;
        else return false;
    }
}
