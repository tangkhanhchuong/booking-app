package com.springboot.booking_app.dto.response;

import com.springboot.booking_app.util.RoomSize;
import com.springboot.booking_app.util.RoomType;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class RoomResponseDTO {

    private UUID id;

    private String roomCode;

    private RoomType roomType;

    private Integer capacity = 0;

    private Boolean isAvailable = true;

    private Integer numberOfBed = 0;

    private RoomSize size = RoomSize.SMALL;

    private UUID buildingId;

    private Instant createdAt;

    private Instant updatedAt;
}
