package com.springboot.booking_app.controller;

import com.springboot.booking_app.dto.request.BaseListingAndSortingRequestDTO;
import com.springboot.booking_app.dto.request.CreateBuildingRequestDTO;
import com.springboot.booking_app.dto.response.BaseCRUDResponseDTO;
import com.springboot.booking_app.dto.response.BuildingResponseDTO;
import com.springboot.booking_app.dto.response.PageResponseDTO;
import com.springboot.booking_app.dto.response.RoomResponseDTO;
import com.springboot.booking_app.model.Building;
import com.springboot.booking_app.model.LandlordTokenPayload;
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

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @PostMapping()
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<BaseCRUDResponseDTO> createBuilding(
        @Valid @RequestBody CreateBuildingRequestDTO bodyDTO,
        @AuthenticationPrincipal LandlordTokenPayload landlordTokenPayload
    ) {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(
                buildingService.createBuilding(bodyDTO,
                    landlordTokenPayload.getUserId()
                )
            );
    }

    @GetMapping()
    @PreAuthorize("hasRole('LANDLORD') or hasRole('TENANT')")
    public ResponseEntity<PageResponseDTO<BuildingResponseDTO>> listBuildings(
        @Valid @ModelAttribute BaseListingAndSortingRequestDTO queryDTO
    ){
        return ResponseEntity.ok(buildingService.listBuildings(queryDTO));
    }

    @GetMapping("{id}")
    @PreAuthorize("hasRole('LANDLORD') or hasRole('TENANT')")
    public ResponseEntity<Building> getBuildingDetail(
        @PathVariable UUID id,
        @AuthenticationPrincipal UserDetails userDetails
    ){
        Building building = buildingService.getBuildingDetail(id);
        return ResponseEntity.ok(building);
    }

    @GetMapping("{id}/rooms")
    @PreAuthorize("hasRole('LANDLORD') or hasRole('TENANT')")
    public ResponseEntity<PageResponseDTO<RoomResponseDTO>> listRoomsOfBuilding(
        @PathVariable UUID id,
        @Valid @ModelAttribute BaseListingAndSortingRequestDTO queryDTO
    ){
        return ResponseEntity.ok(buildingService.listRoomsOfBuilding(id, queryDTO));
    }
}
