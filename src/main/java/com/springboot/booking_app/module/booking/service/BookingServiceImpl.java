package com.springboot.booking_app.module.booking.service;

import com.springboot.booking_app.common.DistributedLockService;
import com.springboot.booking_app.dto.request.CreateBookingRequestDTO;
import com.springboot.booking_app.dto.response.CreateBookingResponseDTO;
import com.springboot.booking_app.entity.Booking;
import com.springboot.booking_app.entity.Room;
import com.springboot.booking_app.entity.User;
import com.springboot.booking_app.exception.exception.RoomIsBookedException;
import com.springboot.booking_app.exception.exception.RoomNotFoundException;
import com.springboot.booking_app.exception.exception.UserNotFoundException;
import com.springboot.booking_app.module.booking.repository.BookingRepository;
import com.springboot.booking_app.module.room.repository.RoomRepository;
import com.springboot.booking_app.module.user.repository.UserRepository;
import com.springboot.booking_app.shared.BookingStatus;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    @Value("${app.booking_lock_time}")
    private Long DEFAULT_REDIS_LOCK_TIME;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    DistributedLockService distributedLockService;

    @Override
    @Transactional
    public CreateBookingResponseDTO createBooking(CreateBookingRequestDTO bodyDTO, UUID userId) {
        Room room = roomRepository.findById(bodyDTO.getRoomId()).orElseThrow(RoomNotFoundException::new);
        if (!room.getIsAvailable()) {
            throw new RoomIsBookedException();
        }

        String idempotentKey = userId + bodyDTO.getRoomId().toString();
        boolean idRequiredLock = distributedLockService.acquireLock(idempotentKey, DEFAULT_REDIS_LOCK_TIME);
        if (idRequiredLock) {
            try {
                User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

                Booking booking = Booking.builder()
                        .user(user)
                        .room(room)
                        .status(BookingStatus.NEW.getStatus())
                        .startAt(bodyDTO.getStartAt())
                        .endAt(bodyDTO.getEndAt())
                        .build();
                bookingRepository.save(booking);

                room.setIsAvailable(false);
                roomRepository.save(room);

                return CreateBookingResponseDTO.builder().bookingId(booking.getId()).build();
            } finally {
                log.info("Lock {} is released", idempotentKey);
                distributedLockService.releaseLock(idempotentKey);
            }
        } else {
            throw new RoomIsBookedException();
        }
    }
}
