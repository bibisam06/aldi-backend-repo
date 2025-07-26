package com.bibisam06.aldi.member.service;

import com.bibisam06.aldi.common.jwt.JwtProvider;
import com.bibisam06.aldi.common.jwt.dto.JwtToken;
import com.bibisam06.aldi.member.dto.AuthRequest;
import com.bibisam06.aldi.member.entity.User;
import com.bibisam06.aldi.member.entity.UserRole;
import com.bibisam06.aldi.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

import static com.bibisam06.aldi.common.constant.StaticValue.BLACKLIST_PREFIX;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    // 로직 - 회원가입
    public JwtToken createUser(AuthRequest request) {
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User newUser = User.builder()
                .userEmail(request.getUserEmail())
                .userPassword(encodedPassword)
                .userRole(UserRole.USER)
                .build();

        userRepository.save(newUser);
        return jwtProvider.generateToken(newUser.getUserId(), newUser.getUserRole());
    }

    public User findByUserEmail(String email) {
        return userRepository.findByUserEmail(email);
    }


    public void logout(String accessToken, String refreshToken) {
        long expiration = jwtProvider.getExpiration(accessToken);
        System.out.println("expiration: " + expiration);
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX+ accessToken, "logout", expiration, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(BLACKLIST_PREFIX + refreshToken, "logout", expiration, TimeUnit.MILLISECONDS);
        redisTemplate.delete("refresh:" + refreshToken);
    }


    public JwtToken login(AuthRequest authRequest) {
        User foundUser = findByUserEmail(authRequest.getUserEmail());

        JwtToken jwtTokens = jwtProvider.generateToken(foundUser.getUserId(), foundUser.getUserRole());

        long expiration = jwtProvider.getExpiration(jwtTokens.getAccessToken());
        long expiration2 = jwtProvider.getExpiration(jwtTokens.getRefreshToken());

        redisTemplate.opsForValue().set("access:" + jwtTokens.getAccessToken(), "login", expiration, TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set("refresh:" + jwtTokens.getRefreshToken(), "login", expiration2, TimeUnit.MILLISECONDS);

        return jwtTokens;
    }

}
