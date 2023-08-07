package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.CommentLike;
import com.ssafy.interviewstudy.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Integer> {

    // 댓글 id에 해당하는 좋아요 모두 반환
    List<CommentLike> findByComment_Id(Integer commentId);

    // 이미 좋아요를 누른 회원인지 체크
    Boolean existsByMember_IdAndComment_Id(Integer memberId, Integer commentId);

    // 댓글마다 좋아요 수 체크
    Integer countByComment(ArticleComment comment);

    void removeCommentLikeByCommentAndMember(ArticleComment comment, Member member);
}
