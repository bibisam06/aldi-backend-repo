package com.bibisam06.aldi.common.redis;

import org.springframework.data.annotation.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@RedisHash(value="blacklist", timeToLive = 3600)
public class BlacklistToken {

    @Id
    private String token;

    private Long expirationTime;

    public BlacklistToken() {}

    public BlacklistToken(String token) {
        this.token = token;
    }

}

