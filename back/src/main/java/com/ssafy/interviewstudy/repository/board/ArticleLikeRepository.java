package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleLike;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Integer> {

    // 이미 좋아요를 누른 회원인지 체크
    Boolean existsByMemberAndArticle(Member member, Board article);
}

