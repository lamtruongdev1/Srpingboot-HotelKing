package com.hotelking.repository;

import com.hotelking.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Integer> {
    @Query("SELECT od FROM OrderDetail od " +
            "WHERE od.room.id = :roomId " +
            "AND ((:dateIn BETWEEN od.dateIn AND od.dateOut) " +
            "OR (:dateOut BETWEEN od.dateIn AND od.dateOut) " +
            "OR (od.dateIn BETWEEN :dateIn AND :dateOut) " +
            "OR (od.dateOut BETWEEN :dateIn AND :dateOut)) " +
            "AND od.order.status = 'BOOKED'")
    List<OrderDetail> findBookedDetails(@Param("roomId") int roomId,
                                        @Param("dateIn") LocalDateTime dateIn,
                                        @Param("dateOut") LocalDateTime dateOut);
}
