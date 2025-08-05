package com.bibisam06.aldi.member.service;

import com.bibisam06.aldi.common.jwt.JwtProvider;
import com.bibisam06.aldi.common.jwt.dto.JwtToken;
import com.bibisam06.aldi.common.jwt.exception.base.BaseException;
import com.bibisam06.aldi.member.dto.AuthRequest;
import com.bibisam06.aldi.member.entity.User;
import com.bibisam06.aldi.member.exception.UserErrorCode;
import com.bibisam06.aldi.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;


    private final RedisTemplate<String, Object> redisTemplate; // 블랙리스트용

    public JwtToken login(AuthRequest authRequest) {
        User foundUser = findByUserEmail(authRequest.getUserEmail());

        if (!passwordEncoder.matches(authRequest.getPassword(), foundUser.getUserPassword())) {
            throw new BaseException(UserErrorCode.INVALID_PASSWORD);
        }

        JwtToken jwtTokens = jwtProvider.generateToken(foundUser.getUserId(), foundUser.getUserRole());

        long accessTokenExp = jwtProvider.getExpiration(jwtTokens.getAccessToken());
        long refreshTokenExp = jwtProvider.getExpiration(jwtTokens.getRefreshToken());


//        accessTokenRedisRepository.saveWithExpiration(
//                userIdToString(foundUser.getUserId()),
//                jwtTokens.getAccessToken(),
//                accessTokenExp
//        );
//
//        refreshTokenRedisRepository.saveWithExpiration(
//                userIdToString(foundUser.getUserId()),
//                jwtTokens.getRefreshToken(),
//                refreshTokenExp
//        );

        return jwtTokens;
    }

    public void logout(String accessToken, String refreshToken) {
        long expiration = jwtProvider.getExpiration(accessToken);

//        redisTemplate.opsForValue().set(
//                "blacklist:" + accessToken,
//                "logout",
//                expiration,
//                TimeUnit.MILLISECONDS
//        );
//
//        redisTemplate.opsForValue().set(
//                "blacklist:" + refreshToken,
//                "logout",
//                expiration,
//                TimeUnit.MILLISECONDS
//        );

        //blacklistTokenRedisRepository.save(refreshToken);

        //refreshTokenRedisRepository.deleteByKey(jwtProvider.getUserIdFromToken(refreshToken).toString());
    }

    public User findByUserEmail(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new BaseException(UserErrorCode.USER_NOT_FOUND));
    }

    public String userIdToString(Integer userId) {
        return userId.toString();
    }

    public void signOut(){

    }
}
