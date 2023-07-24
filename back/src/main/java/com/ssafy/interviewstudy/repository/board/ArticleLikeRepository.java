package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleLike;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleLikeRepository extends JpaRepository<ArticleLike, Integer> {
//    Boolean existsByMember_MemberIdAndArticle_Id(Integer memberId, Integer articleId);
    Boolean existsByMemberAndArticle(Member member, Board article);
}

