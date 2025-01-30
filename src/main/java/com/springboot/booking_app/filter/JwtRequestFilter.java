package com.springboot.booking_app.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.booking_app.common.JwtService;
import com.springboot.booking_app.common.SessionService;
import com.springboot.booking_app.exception.exception.UnauthorizedException;
import com.springboot.booking_app.shared.UserRole;
import com.springboot.booking_app.shared.auth.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authHeader.split("Bearer ")[1];
        final Claims claims = jwtService.extractAllClaims(jwt);
        final String key = claims.getSubject();
        final String userId = claims.get("userId", String.class);
        final UserRole role = claims.get("type", String.class).equals("LANDLORD") ? UserRole.LANDLORD : UserRole.TENANT;

        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String rawSessionPayload = sessionService.getSession(key);
            if (rawSessionPayload == null) {
                throw new UnauthorizedException();
            }

            BaseSessionPayload sessionPayload = readRawSessionPayload(rawSessionPayload, role);
            BaseTokenPayload tokenPayload = mapTokenPayload(UUID.fromString(userId), role);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                tokenPayload,
                null,
                List.of(new SimpleGrantedAuthority("ROLE_" + role.name()))
            );
            authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
    }

    private BaseTokenPayload mapTokenPayload(UUID userId, UserRole role) {
        return switch (role) {
            case LANDLORD -> LandlordTokenPayload.builder().userId(userId).build();
            case TENANT -> TenantTokenPayload.builder().userId(userId).build();
            default -> null;
        };
    }

    private BaseSessionPayload readRawSessionPayload(String rawPayload, UserRole role) throws JsonProcessingException {
        return switch (role) {
            case LANDLORD -> objectMapper.readValue(rawPayload, LandlordSessionPayload.class);
            case TENANT -> objectMapper.readValue(rawPayload, TenantSessionPayload.class);
            default -> null;
        };
    }
}
