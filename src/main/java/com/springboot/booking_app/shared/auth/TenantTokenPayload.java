package com.springboot.booking_app.shared.auth;

import com.springboot.booking_app.shared.UserRole;
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
