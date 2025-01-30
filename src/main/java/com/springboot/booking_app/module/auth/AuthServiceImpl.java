package com.springboot.booking_app.module.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.booking_app.common.JwtService;
import com.springboot.booking_app.common.SessionService;
import com.springboot.booking_app.common.http.UserClient;
import com.springboot.booking_app.dto.request.CreateUserRequestDTO;
import com.springboot.booking_app.dto.request.LoginRequestDTO;
import com.springboot.booking_app.dto.request.RegisterRequestDTO;
import com.springboot.booking_app.dto.response.BaseCRUDResponseDTO;
import com.springboot.booking_app.dto.response.LoginResponseDTO;
import com.springboot.booking_app.dto.response.RegisterResponseDTO;
import com.springboot.booking_app.entity.User;
import com.springboot.booking_app.exception.exception.InvalidLoginException;
import com.springboot.booking_app.exception.exception.UserExistedException;
import com.springboot.booking_app.module.user.UserService;
import com.springboot.booking_app.shared.auth.BaseSessionPayload;
import com.springboot.booking_app.shared.auth.LandlordSessionPayload;
import com.springboot.booking_app.shared.auth.TenantSessionPayload;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${app.access_token_lifespan}")
    private int ACCESS_TOKEN_LIFESPAN;

    final private BCryptPasswordEncoder passwordEncoder;

    final private AuthenticationManager authenticationManager;

    final private JwtService jwtService;

    final private SessionService sessionService;

    final private UserClient userClient;

    final private UserService userService;

    private final ObjectMapper objectMapper;

    public AuthServiceImpl(
            BCryptPasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            UserClient userClient,
            SessionService sessionService,
            UserService userService,
            ObjectMapper objectMapper
    ) {
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.sessionService = sessionService;
        this.userClient = userClient;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @Override
    public RegisterResponseDTO register(RegisterRequestDTO bodyDTO) {
        try {
            String encodedPassword = passwordEncoder.encode(bodyDTO.getPassword());

            BaseCRUDResponseDTO createdUserResponse = this.userClient.createUser(
                CreateUserRequestDTO
                    .builder()
                    .username(bodyDTO.getUsername())
                    .email(bodyDTO.getEmail())
                    .password(encodedPassword)
                    .build()
            );
            if (createdUserResponse == null) {
                throw new RuntimeException();
            }

            return RegisterResponseDTO.builder().id(createdUserResponse.getId()).build();
        } catch (FeignException.FeignClientException.Conflict e) {
            log.error("Register::UserExisted::{}", bodyDTO.getUsername());
            throw new UserExistedException();
        } catch (RuntimeException e) {
            log.error("Register::RuntimeException::{}", e.getMessage());
            throw e;
        }
    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO bodyDTO)  {
        try {
            Optional<User> foundUser = userService.findUserByUsername(bodyDTO.getUsername());
            if (foundUser.isEmpty()) {
                throw new InvalidLoginException();
            }

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
            log.error("Login::Incorrect username or password");
            throw new InvalidLoginException();
        } catch (JsonProcessingException e) {
            log.error("Login::InvalidLoginException::{}", e.getMessage());
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            log.error("Login::RuntimeException::{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
