package com.springboot.booking_app.service;

import com.springboot.booking_app.dto.request.CreateBuildingRequestDTO;
import com.springboot.booking_app.dto.response.BaseCRUDResponseDTO;
import com.springboot.booking_app.dto.response.PageResponseDTO;
import com.springboot.booking_app.dto.response.RoomResponseDTO;
import com.springboot.booking_app.model.Building;

import java.util.List;
import java.util.UUID;

public interface BuildingService {

    public List<Building> listBuildings();

    public PageResponseDTO<RoomResponseDTO> listRoomsOfBuilding(UUID buildingId);

    public Building getBuildingDetail(UUID id);

    public BaseCRUDResponseDTO createBuilding(CreateBuildingRequestDTO bodyDTO);

    public Building updateBuilding(UUID id);

    public boolean deleteBuilding(UUID id);
}
