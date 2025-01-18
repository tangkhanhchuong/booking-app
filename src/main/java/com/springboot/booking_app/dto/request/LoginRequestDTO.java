package com.springboot.booking_app.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequestDTO {

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 100)
    String username;

    @NotNull
    @NotEmpty
    String password;

}
