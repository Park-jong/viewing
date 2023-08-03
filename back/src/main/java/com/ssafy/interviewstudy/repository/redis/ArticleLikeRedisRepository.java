package com.ssafy.interviewstudy.repository.redis;

import com.ssafy.interviewstudy.domain.redis.ArticleLikeRedis;
import org.springframework.data.repository.CrudRepository;

public interface ArticleLikeRedisRepository extends CrudRepository<ArticleLikeRedis, Integer> {

}
