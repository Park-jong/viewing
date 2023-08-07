package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleLike;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Integer> {

    @Query("select distinct b from Board b join fetch b.likes")
    List<Board> findArticleLikeByAllArticle();

    List<ArticleLike> findByArticle_Id(Integer articleId);

    // 이미 좋아요를 누른 회원인지 체크
    Boolean existsByMember_IdAndArticle_Id(Integer memberId, Integer articleId);

    // 글마다 좋아요 수 체크
    Integer countByArticle(Board article);

    @Transactional
    void removeByArticleAndMember(Board article, Member member);
}