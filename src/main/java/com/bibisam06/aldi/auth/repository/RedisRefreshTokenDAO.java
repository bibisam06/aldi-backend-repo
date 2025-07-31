package com.bibisam06.aldi.auth.repository;

import com.bibisam06.aldi.auth.BaseRedisRepository;
import com.bibisam06.aldi.common.annotation.RedisRepository;
import com.bibisam06.aldi.common.jwt.dto.RefreshToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static com.bibisam06.aldi.common.constant.StaticValue.REFRESH_TOKEN_PREFIX;

@RedisRepository
public class RedisRefreshTokenDAO extends BaseRedisRepository<RefreshToken> {

    @Autowired
    public void RedisRefreshTokenDao(RedisTemplate<String, RefreshToken> redisTemplate) {
        this.prefix = REFRESH_TOKEN_PREFIX; // 리프레시 토큰의 접두사 설정
        this.redisTemplate = redisTemplate;
    }


}
