package com.springboot.booking_app.dto.request;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class CreateBookingRequestDTO {

    @NotNull
    UUID roomId;

    @NotNull
    Instant startAt;

    @NotNull
    Instant endAt;
}