package com.bibisam06.aldi.redis;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RedisTemplateTests {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("Redis 로직 잘 작동하나 테스트")
    void redisTemplateString() {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();

        String key = "name";
        valueOperations.set(key, "giraffe");
        String value = valueOperations.get(key);
        assertEquals("giraffe", value);
    }
}