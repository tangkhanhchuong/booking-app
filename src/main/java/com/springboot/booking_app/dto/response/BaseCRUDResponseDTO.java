package com.springboot.booking_app.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class BaseCRUDResponseDTO {

    @NotNull
    UUID id;
}
