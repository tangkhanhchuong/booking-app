package com.springboot.booking_app.model;

import com.springboot.booking_app.util.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class LandlordTokenPayload extends BaseTokenPayload {

    @NotNull
    UUID userId;

    @NotNull
    final UserRole role = UserRole.LANDLORD;

}
