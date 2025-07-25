package com.bibisam06.aldi.auth;

import com.bibisam06.aldi.common.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProvider jwtProvider;

    public void logout(String accessToken) {
        // 1. accessToken에서 만료 시간 추출
        long expiration = jwtProvider.getExpiration(accessToken);

        // 2. 블랙리스트로 저장
        redisTemplate.opsForValue().set("blacklist:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
    }
}
