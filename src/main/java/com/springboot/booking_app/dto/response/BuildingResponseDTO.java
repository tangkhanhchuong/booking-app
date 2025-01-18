package com.springboot.booking_app.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class BuildingResponseDTO {

    private UUID id;

    private String name;

    private String city;

    private String country;

    private String address;

    private Float latitude;

    private Float longitude;

    private UUID ownerId;

    private Instant createdAt;

    private Instant updatedAt;
}
