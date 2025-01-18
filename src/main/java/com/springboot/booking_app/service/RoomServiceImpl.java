package com.springboot.booking_app.service;

import com.springboot.booking_app.dto.request.CreateRoomRequestDTO;
import com.springboot.booking_app.dto.response.BaseCRUDResponseDTO;
import com.springboot.booking_app.exception.exception.BuildingNotFoundException;
import com.springboot.booking_app.exception.exception.RoomExistedException;
import com.springboot.booking_app.exception.exception.RoomNotFoundException;
import com.springboot.booking_app.exception.exception.UserNotFoundException;
import com.springboot.booking_app.model.Building;
import com.springboot.booking_app.model.Room;
import com.springboot.booking_app.model.User;
import com.springboot.booking_app.repository.BuildingRepository;
import com.springboot.booking_app.repository.RoomRepository;
import com.springboot.booking_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Override
    public Room getRoomDetail(UUID id) {
        return roomRepository.findById(id).orElseThrow(RoomNotFoundException::new);
    }

    @Override
    public BaseCRUDResponseDTO createRoom(
        CreateRoomRequestDTO bodyDTO,
        UUID ownerId
    ) {
        User owner = userRepository.findById(ownerId).orElseThrow(UserNotFoundException::new);
        Building foundBuilding = buildingRepository
            .findById(bodyDTO.getBuildingId())
            .orElseThrow(BuildingNotFoundException::new);

        Optional<Room> foundRoom = roomRepository
            .findByRoomCode(bodyDTO.getRoomCode());

        if (foundRoom.isPresent()) {
            throw new RoomExistedException();
        }

        Room newRoom = Room.builder()
            .roomCode(bodyDTO.getRoomCode())
            .roomType(bodyDTO.getRoomType())
            .size(bodyDTO.getSize())
            .capacity(bodyDTO.getCapacity())
            .numberOfBed(bodyDTO.getNumberOfBed())
            .isAvailable(bodyDTO.getIsAvailable())
            .building(foundBuilding)
            .owner(owner)
            .build();
        roomRepository.save(newRoom);
        return BaseCRUDResponseDTO.builder()
                .id(newRoom.getId())
                .build();
    }

    @Override
    public BaseCRUDResponseDTO updateRoom(UUID id) {
        return null;
    }

    @Override
    public boolean deleteRoom(UUID id) {
        return false;
    }
}
