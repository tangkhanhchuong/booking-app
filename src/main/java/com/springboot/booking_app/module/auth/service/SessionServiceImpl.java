package com.springboot.booking_app.module.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class SessionServiceImpl implements SessionService {
    @Autowired
    private RedisTemplate<String, String> template;

    public void addSession(String key, String sessionPayload, int expirationTime) {
        template.opsForValue().set(key, sessionPayload, expirationTime, TimeUnit.MINUTES);
    }

    public String getSession(String key) {
        return template.opsForValue().get(key);
    }

    public void removeSession(String key) {
        template.delete(key);
    }
}
