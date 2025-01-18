package com.springboot.booking_app.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.springboot.booking_app.util.RoomSize;
import com.springboot.booking_app.util.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.UUID;

@Entity
@Data
@Builder
@Table(name = "room")
@AllArgsConstructor
@NoArgsConstructor
public class Room {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "room_code")
    private String roomCode;

    @Column(name = "room_type")
    private RoomType roomType;

    @Column(name = "capacity")
    private Integer capacity = 0;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "number_of_bed")
    private Integer numberOfBed = 0;

    @Column(name = "size")
    private RoomSize size = RoomSize.SMALL;

    @ManyToOne
    @JoinColumn(name = "building_id", nullable = false)
    @JsonProperty("building")
    private Building building;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    @JsonProperty("owner")
    private User owner;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;
}
