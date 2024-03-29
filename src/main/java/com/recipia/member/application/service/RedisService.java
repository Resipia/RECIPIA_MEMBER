package com.recipia.member.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Redis 서비스
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    public void setValues(String key, String data) {
        stringRedisTemplate.opsForValue().set(key, data);
    }

    /**
     * key, value를 duration까지 지정헤서 저장하는 메서드
     */
    public void setValues(String key, String data, Duration duration) {
        stringRedisTemplate.opsForValue().set(key, data, duration);
    }

    /**
     * key값으로 value 가져오기
     */
    @Transactional(readOnly = true)
    public String getValues(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * key값으로 value 삭제하기
     */
    public void deleteValues(String key) {
        stringRedisTemplate.delete(key);
    }

    public void expireValues(String key, int timeout) {
        stringRedisTemplate.expire(key, timeout, TimeUnit.MILLISECONDS);
    }

    public void setHashOps(String key, Map<String, String> data) {
        HashOperations<String, String, String> values = stringRedisTemplate.opsForHash();
        values.putAll(key, data);
    }

    @Transactional(readOnly = true)
    public String getHashOps(String key, String hashKey) {
        HashOperations<String, String, String> values = stringRedisTemplate.opsForHash();
        return values.hasKey(key, hashKey) ? values.get(key, hashKey) : "";
    }

    public void deleteHashOps(String key, String hashKey) {
        HashOperations<String, String, String> values = stringRedisTemplate.opsForHash();
        values.delete(key, hashKey);
    }

    public boolean checkExistsValue(String key) {
        return stringRedisTemplate.hasKey(key);
    }
}
