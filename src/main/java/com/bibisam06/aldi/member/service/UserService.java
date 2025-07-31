package com.bibisam06.aldi.member.service;

import com.bibisam06.aldi.common.jwt.JwtProvider;
import com.bibisam06.aldi.common.jwt.dto.JwtToken;
import com.bibisam06.aldi.common.jwt.exception.base.BaseException;
import com.bibisam06.aldi.member.dto.AuthRequest;
import com.bibisam06.aldi.member.entity.User;
import com.bibisam06.aldi.member.entity.UserRole;
import com.bibisam06.aldi.member.exception.UserErrorCode;
import com.bibisam06.aldi.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static com.bibisam06.aldi.common.constant.StaticValue.BLACKLIST_TOKEN_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    // 로직 - 회원가입
    public JwtToken createUser(AuthRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User foundUser = findByUserEmail(request.getUserEmail());
        if (foundUser != null) {
            throw new BaseException(UserErrorCode.EMAIL_ALREADY_EXISTS);
        }

        User newUser = User.builder()
                .userEmail(request.getUserEmail())
                .userPassword(encodedPassword)
                .userRole(UserRole.USER)
                .build();

        userRepository.save(newUser);
        return jwtProvider.generateToken(newUser.getUserId(), newUser.getUserRole());
    }

    public User findByUserEmail(String email) {
        return userRepository.findByUserEmail(email)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));
    }

    public void logout(String accessToken, String refreshToken) {
        long expiration = jwtProvider.getExpiration(accessToken);
        System.out.println("expiration: " + expiration);
        redisTemplate.opsForValue().set(BLACKLIST_TOKEN_PREFIX+ accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(BLACKLIST_TOKEN_PREFIX + refreshToken, "logout", expiration, TimeUnit.MILLISECONDS);
        redisTemplate.delete("refresh:" + refreshToken);
    }

    public JwtToken login(AuthRequest authRequest) {
        User foundUser = Optional.ofNullable(findByUserEmail(authRequest.getUserEmail()))
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(authRequest.getPassword(), foundUser.getUserPassword())) {
            throw new BaseException(UserErrorCode.INVALID_PASSWORD);
        }

        JwtToken jwtTokens = jwtProvider.generateToken(foundUser.getUserId(), foundUser.getUserRole());

        long accessTokenExp = jwtProvider.getExpiration(jwtTokens.getAccessToken());
        long refreshTokenExp = jwtProvider.getExpiration(jwtTokens.getRefreshToken());

        redisTemplate.opsForValue().set(
                getAccessTokenKey(foundUser.getUserId()),
                jwtTokens.getAccessToken(),
                accessTokenExp,
                TimeUnit.MILLISECONDS
        );

        redisTemplate.opsForValue().set(
                getRefreshTokenKey(foundUser.getUserId()),
                jwtTokens.getRefreshToken(),
                refreshTokenExp,
                TimeUnit.MILLISECONDS
        );

        return jwtTokens;
    }

    private String getAccessTokenKey(Integer userId) {
        return "auth:access:" + userId;
    }

    private String getRefreshTokenKey(Integer userId) {
        return "auth:refresh:" + userId;
    }


}
