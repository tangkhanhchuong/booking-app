package com.springboot.booking_app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateBuildingRequestDTO {

    @NotBlank
    @Size(min = 2, max = 100)
    String name;

    @NotBlank
    @Size(min = 2, max = 100)
    String country;

    @NotBlank
    @Size(min = 2, max = 100)
    String city;

    @NotBlank
    @Size(min = 2, max = 100)
    String address;

    @Min(value = 0)
    Float longitude;

    @Min(value = 0)
    Float latitude;
}
