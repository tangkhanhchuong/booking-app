package com.springboot.booking_app.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class CreateBookingResponseDTO {

    UUID bookingId;
}