package com.ssafy.interviewstudy.domain.redis;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash("article")
public class ArticleLikeRedis {

    @Id
    private Integer articleId;

    private Integer memberId;

    @Builder
    public ArticleLikeRedis(Integer articleId, Integer memberId) {
        this.articleId = articleId;
        this.memberId = memberId;
    }
}
