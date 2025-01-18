package com.springboot.booking_app.shared.auth;

import com.springboot.booking_app.shared.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class LandlordTokenPayload extends BaseTokenPayload {

    @NotNull
    String key;

    @NotNull
    UUID userId;

    @NotNull
    final UserRole role = UserRole.LANDLORD;

}
