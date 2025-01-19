package com.springboot.booking_app.module.booking;

import com.springboot.booking_app.dto.request.CreateBookingRequestDTO;
import com.springboot.booking_app.dto.response.CreateBookingResponseDTO;
import com.springboot.booking_app.module.booking.service.BookingService;
import com.springboot.booking_app.shared.auth.TenantTokenPayload;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping()
    @PreAuthorize("hasRole('TENANT')")
    public CreateBookingResponseDTO createBooking(
            @Valid @RequestBody CreateBookingRequestDTO bodyDTO,
            @AuthenticationPrincipal TenantTokenPayload tenantTokenPayload
        ) {
        return bookingService.createBooking(bodyDTO, tenantTokenPayload.getUserId());
    }
}
