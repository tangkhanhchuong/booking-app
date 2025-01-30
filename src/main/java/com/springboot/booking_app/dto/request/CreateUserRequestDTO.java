package com.springboot.booking_app.dto.request;

import lombok.*;
import jakarta.validation.constraints.*;

@Getter()
@Setter
@Builder
public class CreateUserRequestDTO {
    @NotNull
    String username;

    @NotNull
    String email;

    @NotNull
    String password;
}
