package com.springboot.booking_app.module.booking.service;

import com.springboot.booking_app.dto.request.CreateBookingRequestDTO;
import com.springboot.booking_app.dto.response.CreateBookingResponseDTO;

import java.util.UUID;

public interface BookingService {

    CreateBookingResponseDTO createBooking(CreateBookingRequestDTO bodyDTO, UUID userId);
}
