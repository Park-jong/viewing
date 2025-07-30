package com.ssafy.interviewstudy.repository.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ArticleCommentRepositoryImpl implements ArticleCommentRepository {

    private final JPAArticleCommentRepository jpaArticleCommentRepository;


    @Override
    public Optional<ArticleComment> findById(Integer id) {
        return jpaArticleCommentRepository.findById(id);
    }

    @Override
    public ArticleComment save(ArticleComment comment) {
        return jpaArticleCommentRepository.save(comment);
    }

    @Override
    public void delete(ArticleComment comment) {
        jpaArticleCommentRepository.delete(comment);
    }

    @Override
    public void deleteById(Integer id) {
        jpaArticleCommentRepository.deleteById(id);
    }

    @Override
    public List<ArticleComment> findByLikes() {
        return jpaArticleCommentRepository.findByLikes();
    }

    @Override
    public List<ArticleComment> findAllByArticle(Board article) {
        return jpaArticleCommentRepository.findAllByArticle(article);
    }

    @Override
    public Integer countByArticle(Board article) {
        return jpaArticleCommentRepository.countByArticle(article);
    }

    @Override
    public Integer countByComment(Integer commentId) {
        return jpaArticleCommentRepository.countByComment(commentId);
    }

    @Override
    public List<ArticleComment> findArticleCommentsByComment_Id(Integer commentId) {
        return jpaArticleCommentRepository.findArticleCommentsByComment_Id(commentId);
    }

    @Override
    public void deleteArticleCommentByAuthor(Member member) {
        jpaArticleCommentRepository.deleteArticleCommentByAuthor(member);
    }
}
