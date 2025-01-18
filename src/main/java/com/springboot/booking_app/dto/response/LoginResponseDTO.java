package com.springboot.booking_app.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDTO {

    String accessToken;

    String refreshToken;
}
