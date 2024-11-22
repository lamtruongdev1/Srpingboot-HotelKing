package com.hotelking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckRoomRequest {
    private LocalDate dateIn;
    private LocalDate dateOut;
    private int adultGuests;
    private int childGuests;
    private int numberOfRooms;
}
