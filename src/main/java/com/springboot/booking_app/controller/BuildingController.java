package com.springboot.booking_app.controller;

import com.springboot.booking_app.dto.request.CreateBuildingRequestDTO;
import com.springboot.booking_app.dto.response.BaseCRUDResponseDTO;
import com.springboot.booking_app.dto.response.PageResponseDTO;
import com.springboot.booking_app.dto.response.RoomResponseDTO;
import com.springboot.booking_app.model.Building;
import com.springboot.booking_app.model.LandlordTokenPayload;
import com.springboot.booking_app.model.TenantTokenPayload;
import com.springboot.booking_app.service.BuildingService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @PostMapping()
    public ResponseEntity<BaseCRUDResponseDTO> createBuilding(@Valid @RequestBody CreateBuildingRequestDTO bodyDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(buildingService.createBuilding(bodyDTO));
    }

    @GetMapping()
    public ResponseEntity<List<Building>> listBuildings(){
        List<Building> buildingList = buildingService.listBuildings();
        return ResponseEntity.ok(buildingList);
    }

    @GetMapping("{id}")
    public ResponseEntity<Building> getBuildingDetail(
        @PathVariable UUID id,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        Building building = buildingService.getBuildingDetail(id);
        return ResponseEntity.ok(building);
    }

    @GetMapping("{id}/rooms")
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<PageResponseDTO<RoomResponseDTO>> listRoomsOfBuilding(
            @PathVariable UUID id,
            @AuthenticationPrincipal LandlordTokenPayload landlordTokenPayload
    ){
        return ResponseEntity.ok(buildingService.listRoomsOfBuilding(id));
    }
}
