package com.springboot.booking_app.service;

import com.springboot.booking_app.util.UserRole;

import java.util.UUID;

public interface JwtService {

    String generateToken(UUID userId, UserRole role);

    String extractUsername(String token);

    String extractSubject(String token);

    boolean isTokenExpired(String token);
}
