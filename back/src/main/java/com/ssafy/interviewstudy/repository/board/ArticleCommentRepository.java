package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Integer> {

    // 댓글 리스트 조회(최신순)
    @Query("select distinct c from ArticleComment c join fetch c.author ca left join c.replies cr left join cr.author where c.comment is null and c.article = :article order by c.createdAt desc")
    List<ArticleComment> findAllByArticle(Board article);

//    @Query("select c from ArticleComment c join fetch c.replies where c.comment.id = :parentId")
//    List<ArticleComment> findRepliesByComment(Integer parentId);

//    SELECT s.*,
//    CASE WHEN b.member_id = 2 THEN '북마크 함' ELSE '북마크 안함' END AS bookmark_status
//    FROM public.study s
//    LEFT JOIN public.study_bookmark b ON s.study_id = b.study_id AND b.member_id = 2

    @Query("SELECT c," +
            "CASE WHEN l.member.id = :memberId THEN 'true' ELSE 'false' END AS isLike " +
            "FROM ArticleComment c " +
            "LEFT JOIN CommentLike l ON c.id = l.comment.id AND l.member.id = :memberId " +
            "WHERE c.article.id = :articleId")
    List<Object[]> findAllWithLikes(Integer memberId, Integer articleId);

    // 글마다 댓글 수 count
    Integer countByArticle(Board article);

//    ArticleComment saveReply(Integer commentId, )

    // 대댓글 수 count
    @Query("select count(c) from ArticleComment c where c.comment.id = :commentId and c.isDelete = false")
    Integer countByComment(Integer commentId);
}
