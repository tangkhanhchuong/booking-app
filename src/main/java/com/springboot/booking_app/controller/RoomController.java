package com.springboot.booking_app.controller;

import com.springboot.booking_app.dto.request.CreateRoomRequestDTO;
import com.springboot.booking_app.dto.response.BaseCRUDResponseDTO;
import com.springboot.booking_app.model.LandlordTokenPayload;
import com.springboot.booking_app.service.RoomService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping()
    @PreAuthorize("hasRole('LANDLORD')")
    public ResponseEntity<BaseCRUDResponseDTO> createRoom(
        @Valid @RequestBody CreateRoomRequestDTO bodyDTO,
        @AuthenticationPrincipal LandlordTokenPayload landlordTokenPayload
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(roomService.createRoom(bodyDTO, landlordTokenPayload.getUserId()));
    }
}
