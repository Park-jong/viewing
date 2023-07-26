package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Integer> {
    List<ArticleComment> findAllByArticle(Board article);
}
