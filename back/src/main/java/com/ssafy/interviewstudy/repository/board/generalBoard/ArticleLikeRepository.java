package com.ssafy.interviewstudy.repository.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.ArticleLike;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.repository.CommonRepository;

import java.util.List;

public interface ArticleLikeRepository extends CommonRepository<ArticleLike, Integer> {
    List<Board> findArticleLikeByAllArticle();

    List<ArticleLike> findByArticle_Id(Integer articleId);

    Boolean existsByMember_IdAndArticle_Id(Integer memberId, Integer articleId);

    Integer countByArticle(Board article);

    void removeByArticleAndMember(Board article, Member member);
}