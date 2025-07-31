package com.bibisam06.aldi.common.annotation;


import org.springframework.stereotype.Component;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 Redis 접근용으로 쓰이는 RedisRepository 어노테이션입니다.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RedisRepository {
    String value()  default "";
}
