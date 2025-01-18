package com.springboot.booking_app.dto.request;

import com.springboot.booking_app.util.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RegisterRequestDTO {

    @NotNull
    @NotEmpty
    @Size(min = 5, max = 25)
    private String username;

    @NotNull
    @NotEmpty
    @Email
    private String email;

    @NotNull
    @NotEmpty
    private String password;

    @NotNull
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.TENANT;

    @NotEmpty
    @Pattern(regexp = "^\\+?[0-9]{10,15}$")
    private String mobile;

    @NotEmpty
    private String address;

    @NotNull
    private boolean isMale = true;
}
