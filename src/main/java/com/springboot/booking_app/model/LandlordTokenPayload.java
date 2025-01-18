package com.springboot.booking_app.model;

import com.springboot.booking_app.util.UserRole;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class LandlordTokenPayload {

    UUID userId;

    UserRole role = UserRole.LANDLORD;


}
