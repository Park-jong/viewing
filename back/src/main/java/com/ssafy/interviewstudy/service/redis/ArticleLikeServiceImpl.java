package com.ssafy.interviewstudy.service.redis;

import com.ssafy.interviewstudy.domain.board.ArticleLike;
import com.ssafy.interviewstudy.domain.board.Board;
import com.ssafy.interviewstudy.domain.member.Member;
import com.ssafy.interviewstudy.repository.board.generalBoard.ArticleLikeRepository;
import com.ssafy.interviewstudy.repository.board.generalBoard.BoardRepository;
import com.ssafy.interviewstudy.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ArticleLikeServiceImpl implements ArticleLikeService {

    private String keySet = "article";
    private String keyString = "article:";
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ArticleLikeRepository articleLikeRepository;
    private final RedisTemplate<String, String> redisTemplate;

    // 캐시에서만 읽어오기 주기적으로 삭제 작업

    // 멤버의 게시글 좋아요 여부 체크
    @Override
    public Boolean checkMemberLikeArticle(Integer articleId, Integer memberId) {
        if (!redisTemplate.hasKey(keySet)) saveLikeArticleListFromDb();

        if (redisTemplate.opsForSet().isMember(keySet, String.valueOf(articleId))
                && !redisTemplate.hasKey(keyString + articleId))
            saveArticleLike(articleId);

        return redisTemplate.opsForSet().isMember(keyString + articleId, String.valueOf(memberId));
    }

    // 좋아요 수 체크(key가 존재하지 않아도 null이 아닌 0 반환)
    @Override
    public Integer getLikeCount(Integer articleId) {
        // 좋아요가 눌린 게시글들의 set 목록에 있지만 키가 존재하지 않는다 => 캐시미스. 다시 불러오기
        if (!redisTemplate.hasKey(keySet)) saveLikeArticleListFromDb();
        if (redisTemplate.opsForSet().isMember(keySet, String.valueOf(articleId))
                && !redisTemplate.hasKey(keyString + articleId)) {
            saveArticleLike(articleId);
        }
        long likeCount = redisTemplate.opsForSet().size(keyString + articleId);
        return (int) likeCount;
    }

    // 좋아요 누르기
    @Override
    public Integer saveArticleLike(Integer articleId, Integer memberId) {
        // 이미 좋아요를 누른 상황이면 0을 반환
        if (checkMemberLikeArticle(articleId, memberId)) return 0;
        redisTemplate.opsForSet().add(keySet, String.valueOf(articleId));
        redisTemplate.opsForSet().add(keyString + articleId, String.valueOf(memberId));
        ArticleLike articleLike = ArticleLike.builder()
                .article(boardRepository.findById(articleId).get())
                .member(memberRepository.findMemberById(memberId))
                .build();
        articleLikeRepository.save(articleLike);
        return articleId;
    }

    // 좋아요 삭제
    @Transactional
    @Override
    public int removeArticleLike(Integer articleId, Integer memberId) {
        if (!checkMemberLikeArticle(articleId, memberId)) return 0;

        long removeCnt = redisTemplate.opsForSet().remove(keyString + articleId, String.valueOf(memberId));
        if (removeCnt > 0) {
            Board board = boardRepository.findById(articleId).get();
            Member member = memberRepository.findMemberById(memberId);
            articleLikeRepository.removeByArticleAndMember(board, member);
        }
        return (int) removeCnt;
    }

    // 해당하는 글 번호에 좋아요를 누른 멤버들 저장
    @Override
    public void saveArticleLike(Integer articleId) {
        List<ArticleLike> likes = articleLikeRepository.findByArticle_Id(articleId);
        for (ArticleLike like : likes) {
            redisTemplate.opsForSet().add(keyString + articleId, String.valueOf(like.getMember().getId()));
        }
    }

    // 캐시에 없으면 DB에서 조회하고 업데이트
    @Override
    public void saveLikeArticleListFromDb() {
        List<Board> articles = articleLikeRepository.findArticleLikeByAllArticle();
        if (articles == null) return;
        for (Board article : articles) {
            redisTemplate.opsForSet().add(keySet, String.valueOf(article.getId()));
        }
    }

    @Scheduled(fixedDelay = 3600000 * 12)
    @Override
    public void deleteAllMembersInSetScheduled() {
        Set<String> articles = redisTemplate.opsForSet().members(keySet);
        for (String key : articles) {
            redisTemplate.delete(keyString + key);
        }
    }

}
