package com.hotelking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookRoomRequest {
    private Date dateIn;
    private Date dateOut;
    private int adultGuests;
    private int childGuests;
    private int numberOfRooms;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String customerAddress;
    private int paymentId;
}

