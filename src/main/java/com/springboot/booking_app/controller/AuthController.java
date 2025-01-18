package com.springboot.booking_app.controller;

import com.springboot.booking_app.dto.request.LoginRequestDTO;
import com.springboot.booking_app.dto.request.RegisterRequestDTO;
import com.springboot.booking_app.dto.response.LoginResponseDTO;
import com.springboot.booking_app.dto.response.RegisterResponseDTO;
import com.springboot.booking_app.repository.UserRepository;
import com.springboot.booking_app.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Slf4j
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO bodyDTO) {
        return ResponseEntity.ok(authService.register(bodyDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO bodyDTO) {
        return ResponseEntity.ok(authService.login(bodyDTO));
    }
}
