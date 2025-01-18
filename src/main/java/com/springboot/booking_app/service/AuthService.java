package com.springboot.booking_app.service;

import com.springboot.booking_app.dto.request.LoginRequestDTO;
import com.springboot.booking_app.dto.request.RegisterRequestDTO;
import com.springboot.booking_app.dto.response.LoginResponseDTO;
import com.springboot.booking_app.dto.response.RegisterResponseDTO;

public interface AuthService {

    RegisterResponseDTO register(RegisterRequestDTO bodyDTO);

    LoginResponseDTO login(LoginRequestDTO bodyDTO);
}
