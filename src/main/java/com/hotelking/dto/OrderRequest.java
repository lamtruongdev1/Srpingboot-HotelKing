package com.hotelking.dto;

import com.hotelking.entity.Customer;
import com.hotelking.entity.Order;
import com.hotelking.entity.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

    private int id;
    private Date date;
    private String products;
    private int quantity;
    private Double amount;
    private String orderInfo;
    private Double totalPrice;
    private String promoteCode;
    private String paymentTime;
    private String transactionId;
    private Customer customer;
    private String note;
    private List<Payment> payments;
}
