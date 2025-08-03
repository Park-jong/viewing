package com.ssafy.interviewstudy.repository.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.ArticleLike;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleLikeRepositoryImpl implements ArticleLikeRepository {

    private final JPAArticleLikeRepository jpaArticleLikeRepository;

    @Override
    public Optional<ArticleLike> findById(Integer id) {
        return jpaArticleLikeRepository.findById(id);
    }

    @Override
    public ArticleLike save(ArticleLike articleLike) {
        return jpaArticleLikeRepository.save(articleLike);
    }

    @Override
    public void delete(ArticleLike articleLike) {
        jpaArticleLikeRepository.delete(articleLike);
    }

    @Override
    public void deleteById(Integer id) {
        jpaArticleLikeRepository.deleteById(id);
    }

    @Override
    public List<Board> findArticleLikeByAllArticle() {
        return jpaArticleLikeRepository.findArticleLikeByAllArticle();
    }

    @Override
    public List<ArticleLike> findByArticle_Id(Integer articleId) {
        return jpaArticleLikeRepository.findByArticle_Id(articleId);
    }

    @Override
    public Boolean existsByMember_IdAndArticle_Id(Integer memberId, Integer articleId) {
        return jpaArticleLikeRepository.existsByMember_IdAndArticle_Id(memberId, articleId);
    }

    @Override
    public Integer countByArticle(Board article) {
        return jpaArticleLikeRepository.countByArticle(article);
    }

    @Override
    public void removeByArticleAndMember(Board article, Member member) {
        jpaArticleLikeRepository.removeByArticleAndMember(article, member);
    }
}
