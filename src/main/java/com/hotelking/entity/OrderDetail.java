package com.hotelking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "OrderDetails")
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDateTime dateIn;

    private LocalDateTime dateOut;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private String products;

    private int quantity;

    private Double amount;

    private String orderInfo;

    private String promoteCode;

    private String vnpPaymentTime;

    private String transactionId;

    private String paymentMethod;


    @Column(columnDefinition = "TEXT")
    private String note;

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "promotionId")
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "paymentId", nullable = false)
    private Payment payment;
}
