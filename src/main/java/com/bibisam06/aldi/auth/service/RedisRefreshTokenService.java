package com.bibisam06.aldi.auth.service;


import com.bibisam06.aldi.auth.repository.RedisRefreshTokenDAO;
import com.bibisam06.aldi.common.jwt.dto.RefreshToken;
import com.bibisam06.aldi.common.jwt.exception.base.BaseException;
import com.bibisam06.aldi.member.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisRefreshTokenService {

    private final RedisRefreshTokenDAO refreshTokenRedisDao;

    public void save(Long id, RefreshToken blacklistToken, Long exp) {
        refreshTokenRedisDao.saveWithExpiration(refreshTokenRedisDao.generateKeyFromId(id),blacklistToken , exp);
    }

    public RefreshToken findById(Long id) {
        return refreshTokenRedisDao.findById(id)
                .orElseThrow(() -> new BaseException(UserErrorCode.EXPIRED_REFRESH_TOKEN));
    }

    public void deleteByKey(String key) {
        refreshTokenRedisDao.deleteByKey(key);
    }
}
