package com.hotelking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room_facility")
public class RoomFacility {

    @EmbeddedId
    private RoomFacilityId id;

    @ManyToOne
    @MapsId("roomId")
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @MapsId("facilityId")
    @JoinColumn(name = "facility_id")
    private Facility facility;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;
}
