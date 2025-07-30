package com.ssafy.interviewstudy.repository.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.repository.CommonRepository;

import java.util.List;

public interface ArticleCommentRepository extends CommonRepository<ArticleComment, Integer> {
    List<ArticleComment> findByLikes();

    List<ArticleComment> findAllByArticle(Board article);

    Integer countByArticle(Board article);

    Integer countByComment(Integer commentId);

    List<ArticleComment> findArticleCommentsByComment_Id(Integer commentId);

    void deleteArticleCommentByAuthor(Member member);
}
