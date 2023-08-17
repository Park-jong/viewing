package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Integer> {

    @Query("select distinct c from ArticleComment c join fetch c.likes")
    List<ArticleComment> findByLikes();

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

    // 댓글ID로 가져오기
    @Query("select ac from ArticleComment ac where ac.comment.id=:commentId")
    List<ArticleComment> findArticleCommentsByComment_Id(Integer commentId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ArticleComment ac set ac.isDelete = true where ac.author = :member")
    void deleteArticleCommentByAuthor(Member member);
}
