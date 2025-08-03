package com.ssafy.interviewstudy.repository.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.ArticleFile;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleFileRepositoryImpl implements ArticleFileRepository{

    private final JPAArticleFileRepository jpaArticleFileRepository;

    @Override
    public Optional<ArticleFile> findById(Integer id) {
        return jpaArticleFileRepository.findById(id);
    }

    @Override
    public ArticleFile save(ArticleFile articleFile) {
        return jpaArticleFileRepository.save(articleFile);
    }

    @Override
    public void delete(ArticleFile articleFile) {
        jpaArticleFileRepository.delete(articleFile);
    }

    @Override
    public void deleteById(Integer id) {
        jpaArticleFileRepository.deleteById(id);
    }

    @Override
    public List<ArticleFile> findByArticle_Id(Integer articleId) {
        return jpaArticleFileRepository.findByArticle_Id(articleId);
    }

    @Override
    public List<ArticleFile> findByStudyArticle_Id(Integer articleId) {
        return jpaArticleFileRepository.findByStudyArticle_Id(articleId);
    }

    @Override
    public Integer removeByArticleId(Integer articleId) {
        return jpaArticleFileRepository.removeByArticleId(articleId);
    }

    @Override
    public Integer removeByStudyArticleId(Integer studyArticleId) {
        return jpaArticleFileRepository.removeByStudyArticleId(studyArticleId);
    }
}
