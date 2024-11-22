package com.hotelking.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomFacilityId implements Serializable {

    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "facility_id")
    private Integer facilityId;
}
