package com.springboot.booking_app.module.auth.service;

import com.springboot.booking_app.shared.UserRole;

import java.util.UUID;

public interface JwtService {

    String generateToken(UUID userId, UserRole role);

    String extractUsername(String token);

    String extractSubject(String token);

    boolean isTokenExpired(String token);
}
