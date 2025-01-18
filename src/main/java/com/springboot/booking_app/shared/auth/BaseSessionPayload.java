package com.springboot.booking_app.shared.auth;

import com.springboot.booking_app.shared.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.UUID;

@Getter
public abstract class BaseSessionPayload {

    @NotNull
    String key;

    @NotNull
    UUID userId;

    @Enumerated(EnumType.STRING)
    @NotNull
    UserRole role;

}
