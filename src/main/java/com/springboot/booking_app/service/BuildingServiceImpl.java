package com.springboot.booking_app.service;

import com.springboot.booking_app.dto.request.BaseListingAndSortingRequestDTO;
import com.springboot.booking_app.dto.request.CreateBuildingRequestDTO;
import com.springboot.booking_app.dto.response.BaseCRUDResponseDTO;
import com.springboot.booking_app.dto.response.BuildingResponseDTO;
import com.springboot.booking_app.dto.response.PageResponseDTO;
import com.springboot.booking_app.dto.response.RoomResponseDTO;
import com.springboot.booking_app.exception.exception.BuildingNotFoundException;
import com.springboot.booking_app.exception.exception.UserNotFoundException;
import com.springboot.booking_app.model.Building;
import com.springboot.booking_app.model.Room;
import com.springboot.booking_app.model.User;
import com.springboot.booking_app.repository.BuildingRepository;
import com.springboot.booking_app.repository.RoomRepository;
import com.springboot.booking_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public PageResponseDTO<BuildingResponseDTO> listBuildings(BaseListingAndSortingRequestDTO queryDTO) {
        Page<Building> buildingPage = buildingRepository.findAll(
            PageRequest.of(queryDTO.getPageNumber(), queryDTO.getPageSize())
        );
        List<BuildingResponseDTO> buildings = buildingPage.getContent().stream().map(
        building -> BuildingResponseDTO
                .builder()
                .id(building.getId())
                .name(building.getName())
                .address(building.getAddress())
                .country(building.getCountry())
                .city(building.getCity())
                .latitude(building.getLatitude())
                .longitude(building.getLongitude())
                .ownerId(building.getOwner().getId())
                .createdAt(building.getCreatedAt())
                .updatedAt(building.getUpdatedAt())
                .build()
            ).toList();
        return PageResponseDTO.<BuildingResponseDTO>builder()
                .items(buildings)
                .totalPages(buildingPage.getTotalPages())
                .totalItems(buildingPage.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<RoomResponseDTO> listRoomsOfBuilding(UUID buildingId, BaseListingAndSortingRequestDTO queryDTO) {
        Page<Room> roomPage = roomRepository.findByBuildingId(
            buildingId,
            PageRequest.of(queryDTO.getPageNumber(), queryDTO.getPageSize())
        );
        List<RoomResponseDTO> rooms = roomPage.getContent().stream().map(
            room -> RoomResponseDTO
                .builder()
                .id(room.getId())
                .roomCode(room.getRoomCode())
                .roomType(room.getRoomType())
                .capacity(room.getCapacity())
                .isAvailable(room.getIsAvailable())
                .numberOfBed(room.getNumberOfBed())
                .size(room.getSize())
                .buildingId(room.getBuilding().getId())
                .createdAt(room.getCreatedAt())
                .updatedAt(room.getUpdatedAt())
                .build()
            ).toList();
        return PageResponseDTO.<RoomResponseDTO>builder()
            .items(rooms)
            .totalPages(roomPage.getTotalPages())
            .totalItems(roomPage.getTotalElements())
            .build();
    }

    @Override
    public Building getBuildingDetail(UUID id) {
        return buildingRepository.findById(id).orElseThrow(BuildingNotFoundException::new);
    }

    @Override
    public BaseCRUDResponseDTO createBuilding(
        CreateBuildingRequestDTO bodyDTO,
        UUID ownerId
    ) {
        User owner = userRepository.findById(ownerId).orElseThrow(UserNotFoundException::new);

        Building newBuilding = Building.builder()
            .name(bodyDTO.getName())
            .city(bodyDTO.getCity())
            .country(bodyDTO.getCountry())
            .address(bodyDTO.getAddress())
            .latitude(bodyDTO.getLatitude())
            .longitude(bodyDTO.getLongitude())
            .owner(owner)
            .build();
        buildingRepository.save(newBuilding);
        return BaseCRUDResponseDTO.builder()
            .id(newBuilding.getId())
            .build();
    }

    @Override
    public Building updateBuilding(UUID id) {
        return null;
    }

    @Override
    public boolean deleteBuilding(UUID id) {
        return false;
    }
}
