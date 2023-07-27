package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Integer> {
    List<ArticleComment> findAllByArticle(Board article);

    @Query("select c from ArticleComment c where c.comment.id = :parentId")
    List<ArticleComment> findRepliesByComment(Integer parentId);

    // 글마다 댓글 수 count
    Integer countByArticle(Board article);

    // 대댓글 수 count
    @Query("select count(c) from ArticleComment c where c.comment.id = :commentId and c.isDelete = false")
    Integer countByComment(Integer commentId);
}
