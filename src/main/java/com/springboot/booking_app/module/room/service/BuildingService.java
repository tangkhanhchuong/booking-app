package com.springboot.booking_app.module.room.service;

import com.springboot.booking_app.dto.request.BaseListingAndSortingRequestDTO;
import com.springboot.booking_app.dto.request.CreateBuildingRequestDTO;
import com.springboot.booking_app.dto.response.BaseCRUDResponseDTO;
import com.springboot.booking_app.dto.response.BuildingResponseDTO;
import com.springboot.booking_app.dto.response.PageResponseDTO;
import com.springboot.booking_app.dto.response.RoomResponseDTO;
import com.springboot.booking_app.entity.Building;

import java.util.UUID;

public interface BuildingService {

    public PageResponseDTO<BuildingResponseDTO> listBuildings(BaseListingAndSortingRequestDTO queryDTO);

    public PageResponseDTO<RoomResponseDTO> listRoomsOfBuilding(UUID buildingId, BaseListingAndSortingRequestDTO queryDTO);

    public Building getBuildingDetail(UUID id);

    public BaseCRUDResponseDTO createBuilding(CreateBuildingRequestDTO bodyDTO, UUID ownerId);

    public Building updateBuilding(UUID id);

    public boolean deleteBuilding(UUID id);
}
