package com.springboot.booking_app.common;

public interface SessionService {

    public void addSession(String key, String sessionPayload, int expirationTime);

    public String getSession(String key);

    public void removeSession(String key);
}
