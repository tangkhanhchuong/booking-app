package com.springboot.booking_app.shared;

import lombok.Data;
import lombok.Getter;

@Getter
public enum BookingStatus {
    NEW("NEW"),
    PENDING("PENDING"),
    PAID("PAID"),
    CANCELLED("CANCELLED");

    private final String status;

    // Constructor
    BookingStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}