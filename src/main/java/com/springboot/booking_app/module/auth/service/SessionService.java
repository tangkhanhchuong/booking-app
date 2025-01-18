package com.springboot.booking_app.module.auth.service;

public interface SessionService {

    public void addSession(String key, String sessionPayload, int expirationTime);

    public String getSession(String key);

    public void removeSession(String key);
}
