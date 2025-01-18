package com.springboot.booking_app.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class BaseListingAndSortingRequestDTO {
    @Min(0)
    Integer pageNumber = 0;

    @NotNull
    @Min(0)
    Integer pageSize = 10;
}
