package com.ssafy.interviewstudy.repository.board.generalBoard;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.CommentLike;
import com.ssafy.interviewstudy.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CommentLikeRepositoryImpl implements CommentLikeRepository {

    private final JPACommentLikeRepository jpaCommentLikeRepository;

    @Override
    public Optional<CommentLike> findById(Integer id) {
        return jpaCommentLikeRepository.findById(id);
    }

    @Override
    public CommentLike save(CommentLike commentLike) {
        return jpaCommentLikeRepository.save(commentLike);
    }

    @Override
    public void delete(CommentLike commentLike) {
        jpaCommentLikeRepository.delete(commentLike);
    }

    @Override
    public void deleteById(Integer id) {
        jpaCommentLikeRepository.findById(id);
    }

    @Override
    public List<CommentLike> findByComment_Id(Integer commentId) {
        return jpaCommentLikeRepository.findByComment_Id(commentId);
    }

    @Override
    public Boolean existsByMember_IdAndComment_Id(Integer memberId, Integer commentId) {
        return jpaCommentLikeRepository.existsByMember_IdAndComment_Id(memberId, commentId);
    }

    @Override
    public Integer countByComment(ArticleComment comment) {
        return jpaCommentLikeRepository.countByComment(comment);
    }

    @Override
    public void removeCommentLikeByCommentAndMember(ArticleComment comment, Member member) {
        jpaCommentLikeRepository.removeCommentLikeByCommentAndMember(comment, member);
    }
}
