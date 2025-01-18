package com.springboot.booking_app.module.auth.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.booking_app.dto.request.LoginRequestDTO;
import com.springboot.booking_app.dto.request.RegisterRequestDTO;
import com.springboot.booking_app.dto.response.LoginResponseDTO;
import com.springboot.booking_app.dto.response.RegisterResponseDTO;
import com.springboot.booking_app.exception.exception.InvalidLoginException;
import com.springboot.booking_app.exception.exception.UserExistedException;
import com.springboot.booking_app.entity.User;
import com.springboot.booking_app.module.user.repository.UserRepository;
import com.springboot.booking_app.shared.auth.BaseSessionPayload;
import com.springboot.booking_app.shared.auth.LandlordSessionPayload;
import com.springboot.booking_app.shared.auth.TenantSessionPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${app.access_token_lifespan}")
    private int ACCESS_TOKEN_LIFESPAN;

    final private UserRepository userRepository;

    final private BCryptPasswordEncoder passwordEncoder;

    final private AuthenticationManager authenticationManager;

    final private JwtService jwtService;

    final private SessionService sessionService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuthServiceImpl(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService,
            SessionService sessionService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.sessionService = sessionService;
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO bodyDTO) {
        Optional<User> foundUser = userRepository.findByUsername(bodyDTO.getUsername());
        if (foundUser.isPresent()) {
            throw new UserExistedException();
        }

        String encodedPassword = passwordEncoder.encode(bodyDTO.getPassword());
        User newUser = User.builder()
                .username(bodyDTO.getUsername())
                .email(bodyDTO.getEmail())
                .password(encodedPassword)
                .dob(bodyDTO.getDob())
                .isMale(bodyDTO.isMale())
                .mobile(bodyDTO.getMobile())
                .address(bodyDTO.getAddress())
                .role(bodyDTO.getRole())
                .build();
        userRepository.save(newUser);
        return RegisterResponseDTO.builder().id(newUser.getId()).build();
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO bodyDTO)  {
        try {
            Authentication authResult = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(bodyDTO.getUsername(), bodyDTO.getPassword())
            );
            User parsedUser = (User)authResult.getPrincipal();
            String token = jwtService.generateToken(
                    parsedUser.getId().toString(),
                    parsedUser.getId(),
                    parsedUser.getRole()
            );

            BaseSessionPayload sessionPayload = switch (parsedUser.getRole()) {
                case TENANT -> TenantSessionPayload.builder()
                        .key(parsedUser.getId().toString())
                        .userId(parsedUser.getId())
                        .build();
                case LANDLORD -> LandlordSessionPayload.builder()
                        .key(parsedUser.getId().toString())
                        .userId(parsedUser.getId())
                        .build();
                default -> throw new InvalidLoginException();
            };

            sessionService.addSession(
                    parsedUser.getId().toString(),
                    objectMapper.writeValueAsString(sessionPayload),
                    ACCESS_TOKEN_LIFESPAN
            );
            return LoginResponseDTO.builder()
                    .accessToken(token)
                    .build();
        } catch (BadCredentialsException ex) {
            throw new InvalidLoginException();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
