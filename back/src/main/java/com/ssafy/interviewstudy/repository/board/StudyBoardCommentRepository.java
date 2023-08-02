package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.board.StudyBoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyBoardCommentRepository extends JpaRepository<StudyBoardComment, Integer> {

    // 댓글 리스트 조회(최신순)
    @Query("select distinct c from StudyBoardComment c join fetch c.author ca left join c.replies cr left join cr.author where c.comment is null and c.article = :article order by c.createdAt desc")
    List<StudyBoardComment> findAllByArticle(StudyBoard article);

    // 글마다 댓글 수 count
    Integer countByArticle(StudyBoard article);

    // 대댓글 수 count
    @Query("select count(c) from StudyBoardComment c where c.comment.id = :commentId and c.isDelete = false")
    Integer countByComment(Integer commentId);
}
