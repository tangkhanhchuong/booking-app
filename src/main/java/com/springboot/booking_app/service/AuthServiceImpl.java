package com.springboot.booking_app.service;

import com.springboot.booking_app.dto.request.LoginRequestDTO;
import com.springboot.booking_app.dto.request.RegisterRequestDTO;
import com.springboot.booking_app.dto.response.LoginResponseDTO;
import com.springboot.booking_app.dto.response.RegisterResponseDTO;
import com.springboot.booking_app.exception.exception.InvalidLoginException;
import com.springboot.booking_app.exception.exception.UserExistedException;
import com.springboot.booking_app.model.User;
import com.springboot.booking_app.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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

    final private UserRepository userRepository;

    final private BCryptPasswordEncoder passwordEncoder;

    final private AuthenticationManager authenticationManager;

    final private JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            BCryptPasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
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
            String token = jwtService.generateToken(parsedUser.getId(), parsedUser.getRole());
            return LoginResponseDTO.builder()
                    .accessToken(token)
                    .build();
        } catch (BadCredentialsException ex) {
            throw new InvalidLoginException();
        }
    }
}
