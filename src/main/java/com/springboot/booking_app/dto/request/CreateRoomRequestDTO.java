package com.springboot.booking_app.dto.request;

import com.springboot.booking_app.shared.RoomSize;
import com.springboot.booking_app.shared.RoomType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateRoomRequestDTO {

    @NotBlank
    @Size(min = 0, max = 100)
    String roomCode;

    @NotNull
    RoomType roomType;

    @Min(value = 1)
    Integer capacity = 1;

    Boolean isAvailable = true;

    @Min(value = 0)
    Integer numberOfBed = 0;

    RoomSize size = RoomSize.SMALL;

    @NotNull
    UUID buildingId;

}
