package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Integer> {

    //  order by c.createdAt, cr.createdAt
    // 댓글 리스트 조회(최신순)

    //@Query("SELECT o FROM Order o LEFT JOIN o.customer c
    // WHERE o.id IN (SELECT DISTINCT o2.id FROM Order o2 LEFT JOIN o2.customer c2 ORDER BY o2.someColumn, c2.anotherColumn)")

//    @Query("select distinct c from ArticleComment c join fetch c.author ca left join c.replies cr " +
//            "left join cr.author where c.comment is null and c.article = :article " +
//            "order by c.createdAt, cr.createdAt")
    @Query("select DISTINCT c from ArticleComment c join fetch c.author ca " +
            "left join c.replies cr left join cr.author cra " +
            "where c.comment is null and c.article = :article " +
            "order by c.createdAt")
    List<ArticleComment> findAllByArticle(Board article);

    // 글마다 댓글 수 count
    Integer countByArticle(Board article);

    // 대댓글 수 count
    @Query("select count(c) from ArticleComment c where c.comment.id = :commentId and c.isDelete = false")
    Integer countByComment(Integer commentId);
}
