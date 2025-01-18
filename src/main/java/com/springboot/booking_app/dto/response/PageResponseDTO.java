package com.springboot.booking_app.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PageResponseDTO<T> {

    private List<T> items;

    private int totalPages;

    private long totalItems;
}
