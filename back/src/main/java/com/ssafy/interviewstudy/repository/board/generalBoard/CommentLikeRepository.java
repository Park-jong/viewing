package com.ssafy.interviewstudy.repository.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.CommentLike;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.repository.CommonRepository;

import java.util.List;

public interface CommentLikeRepository extends CommonRepository<CommentLike, Integer> {

    List<CommentLike> findByCommentId(Integer commentId);

    Boolean existsByMemberIdAndCommentId(Integer memberId, Integer commentId);

    Integer countByComment(ArticleComment comment);

    void removeCommentLikeByCommentAndMember(ArticleComment comment, Member member);
}
