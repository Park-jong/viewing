package com.ssafy.interviewstudy.repository.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import com.ssafy.interviewstudy.repository.CommonRepository;

import java.util.List;

public interface ArticleFileRepository extends CommonRepository<ArticleFile, Integer> {
    List<ArticleFile> findByArticle_Id(Integer articleId);

    List<ArticleFile> findByStudyArticle_Id(Integer articleId);

    Integer removeByArticleId(Integer articleId);

    Integer removeByStudyArticleId(Integer studyArticleId);
}
