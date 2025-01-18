package com.springboot.booking_app.module.auth.service;

import com.springboot.booking_app.shared.UserRole;
import io.jsonwebtoken.Claims;

import java.util.UUID;

public interface JwtService {

    String generateToken(String key, UUID userId, UserRole role);

    public Claims extractAllClaims(String token);

    boolean isTokenExpired(String token);
}
