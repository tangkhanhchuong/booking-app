package com.springboot.booking_app.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
public class DistributedLockServiceImpl implements DistributedLockService {

    private static final String LOCK_VALUE = "1";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private String generateLockKey(String idempotentKey) {
        return String.format("Lock:%s", idempotentKey);
    }

    @Override
    public boolean acquireLock(String idempotentKey, Long lockTime) {
        String lockKey = generateLockKey(idempotentKey);
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent(
                lockKey,
                LOCK_VALUE,
                Duration.ofMinutes(lockTime)
            )
        );
    }

    @Override
    public void releaseLock(String idempotentKey) {
        String lockKey = generateLockKey(idempotentKey);
        redisTemplate.delete(lockKey);
    }
}
