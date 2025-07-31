package com.bibisam06.aldi.auth;

import com.bibisam06.aldi.common.jwt.exception.GlobalErrorCode;
import com.bibisam06.aldi.common.jwt.exception.base.BaseException;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public abstract class BaseRedisRepository<T> {
    protected RedisTemplate<String, T> redisTemplate;
    protected String prefix;

    public void save(Long id, T t) {
        try {
            redisTemplate.opsForValue().set(generateKeyFromId(id), t); // T 객체를 그대로 저장 (RedisTemplate이 직렬화 처리)
        } catch (Exception e) {
            throw new BaseException(GlobalErrorCode.REDIS_SAVE_FAILED);
        }
    }

    // 만료 시간 포함한 save 함수
    public void saveWithExpiration(String keyName , T t, long expirationTime){
        try{
            redisTemplate.opsForValue().set(keyName,t,expirationTime, TimeUnit.MILLISECONDS);
        } catch (Exception e){
            throw new BaseException(GlobalErrorCode.REDIS_SAVE_FAILED);
        }
    }

    public Optional<T> findById(Long id) {
        T value = redisTemplate.opsForValue().get(generateKeyFromId(id));
        return Optional.ofNullable(value);
    }

    public boolean hasKey(String keyName){
        return redisTemplate.hasKey(generateKey(keyName));
    }

    public void deleteByKey(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            throw new BaseException(GlobalErrorCode.REDIS_DELETE_FAILED);
        }
    }

    public String generateKeyFromId(Long id) {
        return prefix + id.toString();
    }

    public String generateKey(String keyName){
        return prefix + keyName;
    }
}
