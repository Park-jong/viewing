package com.ssafy.interviewstudy.repository.board;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ArticleFileRepository extends JpaRepository<ArticleFile, Integer> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ArticleFile af where af.article.id = :articleId")
    Integer removeByArticleId(Integer articleId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from ArticleFile af where af.studyArticle.id = :studyArticleId")
    Integer removeByStudyArticleId(Integer studyArticleId);
}
