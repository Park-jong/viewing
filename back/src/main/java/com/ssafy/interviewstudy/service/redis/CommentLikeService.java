package com.ssafy.interviewstudy.service.redis;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface CommentLikeService {
    // 멤버의 게시글 좋아요 여부 체크
    Boolean checkMemberLikeComment(Integer commentId, Integer memberId);

    // 댓글 목록 좋아요 수 일괄 조회
    Map<Integer, Integer> getLikeCountBatch(List<Integer> commentIds);

    // 멤버가 좋아요한 댓글 ID 집합 일괄 조회
    Set<Integer> getLikedCommentIdSet(List<Integer> commentIds, Integer memberId);

    // 좋아요 수 체크(key가 존재하지 않아도 null이 아닌 0 반환)
    Integer getLikeCount(Integer commentId);

    // 좋아요 누르기
    Integer saveCommentLike(Integer commentId, Integer memberId);

    // 좋아요 삭제
    @Transactional
    int removeCommentLike(Integer commentId, Integer memberId);

    // 해당하는 글 번호에 좋아요를 누른 멤버들 저장
    void saveCommentLike(Integer commentId);

    // 캐시에 없으면 DB에서 조회하고 업데이트
    void saveLikeCommentListFromDb();

    @Scheduled(fixedDelay = 3600000 * 12)
        // 매 12시간마다 삭제 작업
    void deleteAllMembersInSetScheduled();
}
