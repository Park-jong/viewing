package com.ssafy.interviewstudy.service.redis;

import com.ssafy.interviewstudy.domain.board.ArticleComment;
import com.ssafy.interviewstudy.domain.board.CommentLike;
import com.ssafy.interviewstudy.exception.board.BoardExceptionFactory;
import com.ssafy.interviewstudy.exception.member.MemberExceptionFactory;
import com.ssafy.interviewstudy.repository.board.generalBoard.ArticleCommentRepository;
import com.ssafy.interviewstudy.repository.board.generalBoard.CommentLikeRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {
    private final String keySet = "comment";
    private final String keyString = "comment:";

    private final ArticleCommentRepository articleCommentRepository;
    private final MemberRepository memberRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final RedisTemplate<String, String> redisTemplate;

    // 캐시에서만 읽어오기 주기적으로 삭제 작업

    // 멤버의 게시글 좋아요 여부 체크
    @Override
    public Boolean checkMemberLikeComment(Integer commentId, Integer memberId) {
        if (!redisTemplate.hasKey(keySet)) saveLikeCommentListFromDb();

        if (redisTemplate.opsForSet().isMember(keySet, String.valueOf(commentId))
                && !redisTemplate.hasKey(keyString + commentId))
            saveCommentLike(commentId);

        return redisTemplate.opsForSet().isMember(keyString + commentId, String.valueOf(memberId));
    }

    // 좋아요 수 체크(key가 존재하지 않아도 null이 아닌 0 반환)
    @Override
    public Integer getLikeCount(Integer commentId) {
        // 좋아요가 눌린 게시글들의 set 목록에 있지만 키가 존재하지 않는다 => 캐시미스. 다시 불러오기
        if (!redisTemplate.hasKey(keySet)) saveLikeCommentListFromDb();

        if (redisTemplate.opsForSet().isMember(keySet, String.valueOf(commentId))
                && !redisTemplate.hasKey(keyString + commentId))
            saveCommentLike(commentId);

        long likeCount = redisTemplate.opsForSet().size(keyString + commentId);
        return (int) likeCount;
    }

    // 댓글 목록 좋아요 수 일괄 조회 (파이프라인)
    @Override
    public Map<Integer, Integer> getLikeCountBatch(List<Integer> commentIds) {
        if (commentIds.isEmpty()) return Collections.emptyMap();
        if (!redisTemplate.hasKey(keySet)) saveLikeCommentListFromDb();
        commentIds.forEach(id -> {
            if (Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(keySet, String.valueOf(id)))
                    && !redisTemplate.hasKey(keyString + id)) {
                saveCommentLike(id);
            }
        });
        List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            for (Integer id : commentIds) {
                connection.sCard((keyString + id).getBytes(StandardCharsets.UTF_8));
            }
            return null;
        });
        Map<Integer, Integer> counts = new HashMap<>();
        for (int i = 0; i < commentIds.size(); i++) {
            Long count = (Long) results.get(i);
            counts.put(commentIds.get(i), count != null ? count.intValue() : 0);
        }
        return counts;
    }

    // 멤버가 좋아요한 댓글 ID 집합 일괄 조회 (파이프라인)
    @Override
    public Set<Integer> getLikedCommentIdSet(List<Integer> commentIds, Integer memberId) {
        if (commentIds.isEmpty() || memberId == null) return Collections.emptySet();
        if (!redisTemplate.hasKey(keySet)) saveLikeCommentListFromDb();
        String memberIdStr = String.valueOf(memberId);
        List<Object> results = redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            byte[] memberBytes = memberIdStr.getBytes(StandardCharsets.UTF_8);
            for (Integer id : commentIds) {
                connection.sIsMember((keyString + id).getBytes(StandardCharsets.UTF_8), memberBytes);
            }
            return null;
        });
        Set<Integer> likedIds = new HashSet<>();
        for (int i = 0; i < commentIds.size(); i++) {
            if (Boolean.TRUE.equals(results.get(i))) {
                likedIds.add(commentIds.get(i));
            }
        }
        return likedIds;
    }

    // 좋아요 누르기
    @Override
    public Integer saveCommentLike(Integer commentId, Integer memberId) {

        // 이미 좋아요를 누른 상황이면 0을 반환
        if (checkMemberLikeComment(commentId, memberId)) return 0;

        // db와 캐시 모두 저장
        redisTemplate.opsForSet().add(keySet, String.valueOf(commentId));
        redisTemplate.opsForSet().add(keyString + commentId, String.valueOf(memberId));
        commentLikeRepository.save(CommentLike.builder()
                .comment(articleCommentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound))
                .member(memberRepository.findMemberById(memberId).orElseThrow(MemberExceptionFactory::memberNotFound))
                .build());
        return commentId;
    }

    // 좋아요 삭제
    @Transactional
    @Override
    public int removeCommentLike(Integer commentId, Integer memberId) {
        if (!checkMemberLikeComment(commentId, memberId)) return 0;

        // db와 캐시 모두 반영
        long removeCnt = redisTemplate.opsForSet().remove(keyString + commentId, String.valueOf(memberId));
        if (removeCnt > 0) {
            commentLikeRepository.removeCommentLikeByCommentAndMember(
                    articleCommentRepository.findById(commentId).orElseThrow(BoardExceptionFactory::commentNotFound),
                    memberRepository.findMemberById(memberId).orElseThrow(MemberExceptionFactory::memberNotFound));
        }
        return (int) removeCnt;
    }

    // 해당하는 글 번호에 좋아요를 누른 멤버들 저장
    @Override
    public void saveCommentLike(Integer commentId) {
        List<CommentLike> likes = commentLikeRepository.findByCommentId(commentId);
        for (CommentLike like : likes) {
            redisTemplate.opsForSet().add(keyString + commentId, String.valueOf(like.getMember().getId()));
        }
    }

    // 캐시에 없으면 DB에서 조회하고 업데이트
    @Override
    public void saveLikeCommentListFromDb() {
        List<ArticleComment> comments = articleCommentRepository.findByLikes();

        if (comments == null) return;
        for (ArticleComment comment : comments) {
            redisTemplate.opsForSet().add(keySet, String.valueOf(comment.getId()));
        }
    }

    @Scheduled(fixedDelay = 3600000 * 12)
    @Override
    public void deleteAllMembersInSetScheduled() {
        Set<String> comments = redisTemplate.opsForSet().members(keySet);
        for (String key : comments) {
            redisTemplate.delete(keyString + key);
        }
    }
}
