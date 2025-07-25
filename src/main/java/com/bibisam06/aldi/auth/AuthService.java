package com.bibisam06.aldi.auth;

import com.bibisam06.aldi.common.jwt.JwtProvider;
import com.bibisam06.aldi.common.jwt.dto.JwtToken;
import com.bibisam06.aldi.member.dto.AuthRequest;
import com.bibisam06.aldi.member.entity.User;
import com.bibisam06.aldi.member.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

import static com.bibisam06.aldi.common.constant.StaticValue.BLACKLIST_PREFIX;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtProvider jwtProvider;

    public void logout(String accessToken, String refreshToken) {
        long expiration = jwtProvider.getExpiration(accessToken);
        System.out.println("expiration: " + expiration);
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX+ accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + refreshToken, "logout", expiration, TimeUnit.MILLISECONDS);
        redisTemplate.delete("refresh:" + refreshToken);
    }


    public JwtToken login(AuthRequest authRequest) {
        User foundUser = userService.findByUserEmail(authRequest.getUserEmail());

        JwtToken jwtTokens = jwtProvider.generateToken(foundUser.getUserId(), foundUser.getUserRole());

        long expiration = jwtProvider.getExpiration(jwtTokens.getAccessToken());
        long expiration2 = jwtProvider.getExpiration(jwtTokens.getRefreshToken());

        redisTemplate.opsForValue().set("access:" + jwtTokens.getAccessToken(), "login", expiration, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set("refresh:" + jwtTokens.getRefreshToken(), "login", expiration2, TimeUnit.MILLISECONDS);

        return jwtTokens;
    }
}
