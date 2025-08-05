package com.bibisam06.aldi.auth.repository;

import com.bibisam06.aldi.auth.BaseRedisRepository;
import com.bibisam06.aldi.common.annotation.RedisRepository;
import com.bibisam06.aldi.common.jwt.dto.BlacklistToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import static com.bibisam06.aldi.common.constant.StaticValue.ACCESS_TOKEN_PREFIX;;

@RedisRepository
public class RedisAccessTokenDAO extends BaseRedisRepository<BlacklistToken> {

    @Autowired
    public void RedisBlacklistTokenDao(RedisTemplate<String, BlacklistToken> redisTemplate) {
        this.prefix = ACCESS_TOKEN_PREFIX;
        this.redisTemplate = redisTemplate;
    }
}
