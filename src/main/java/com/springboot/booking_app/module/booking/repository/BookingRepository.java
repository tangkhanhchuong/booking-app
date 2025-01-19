package com.springboot.booking_app.module.booking.repository;

import com.springboot.booking_app.entity.Booking;
import com.springboot.booking_app.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {
}
