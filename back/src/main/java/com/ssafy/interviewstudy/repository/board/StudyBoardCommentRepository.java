package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.StudyBoard;
import com.ssafy.interviewstudy.domain.board.StudyBoardComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StudyBoardCommentRepository extends JpaRepository<StudyBoardComment, Integer> {

    List<StudyBoardComment> findAllByArticle(StudyBoard article);

    @Query("select c from StudyBoardComment c where c.comment.id = :parentId")
    List<StudyBoardComment> findRepliesByComment(Integer parentId);

    // 글마다 댓글 수 count
    Integer countByArticle(StudyBoard article);

    // 대댓글 수 count
    @Query("select count(c) from ArticleComment c where c.comment.id = :commentId and c.isDelete = false")
    Integer countByComment(Integer commentId);
}
