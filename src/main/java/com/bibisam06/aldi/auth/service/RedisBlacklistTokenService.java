package com.bibisam06.aldi.auth.service;

import com.bibisam06.aldi.auth.repository.RedisBlacklistTokenDAO;
import com.bibisam06.aldi.common.jwt.dto.BlacklistToken;
import com.bibisam06.aldi.common.jwt.exception.base.BaseException;
import com.bibisam06.aldi.member.exception.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/*
 실제 컨트롤러에서 사용하는 서비스 로직입니다.
 */
@Service
@RequiredArgsConstructor
public class RedisBlacklistTokenService {
    private final RedisBlacklistTokenDAO blacklistTokenRedisDao;

    public void save(Long id, BlacklistToken blacklistToken, Long exp) {
        blacklistTokenRedisDao.saveWithExpiration(blacklistTokenRedisDao.generateKeyFromId(id),blacklistToken , exp);
    }

    public BlacklistToken findById(Long id) {
        return blacklistTokenRedisDao.findById(id)
                .orElseThrow(() -> new BaseException(UserErrorCode.EXPIRED_REFRESH_TOKEN));
    }

    public void deleteByKey(String key) {
        blacklistTokenRedisDao.deleteByKey(key);
    }
}
