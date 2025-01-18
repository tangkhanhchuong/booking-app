package com.springboot.booking_app.module.room.service;

import com.springboot.booking_app.dto.request.CreateRoomRequestDTO;
import com.springboot.booking_app.dto.response.BaseCRUDResponseDTO;
import com.springboot.booking_app.entity.Room;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface RoomService {

    public Room getRoomDetail(UUID id);

    public BaseCRUDResponseDTO createRoom(CreateRoomRequestDTO bodyDTO, UUID userId);

    public BaseCRUDResponseDTO updateRoom(UUID id);

    public boolean deleteRoom(UUID id);
}
