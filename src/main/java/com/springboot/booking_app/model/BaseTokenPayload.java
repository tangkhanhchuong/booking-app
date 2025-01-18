package com.springboot.booking_app.model;

import com.springboot.booking_app.util.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public abstract class BaseTokenPayload {

    @NotNull
    UUID userId;

    @Enumerated(EnumType.STRING)
    @NotNull
    UserRole role;

}
