package com.springboot.booking_app.model;

import com.springboot.booking_app.util.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class TenantTokenPayload extends BaseTokenPayload {

    @NotNull
    UUID userId;

    @NotNull
    final UserRole role = UserRole.TENANT;
}
