package com.hotelking.repository;

import com.hotelking.entity.Facility;
import com.hotelking.entity.Room;
import jakarta.transaction.Transactional;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    Optional<Room> findBySlug(String slug);

    @Query("SELECT r FROM Room r WHERE r.status = true " +
            "AND r.id NOT IN (" +
            "  SELECT od.room.id FROM OrderDetail od " +
            "  JOIN od.order o " +
            "  WHERE (" +
            "    (:dateIn BETWEEN od.dateIn AND od.dateOut) " +
            "    OR (:dateOut BETWEEN od.dateIn AND od.dateOut) " +
            "    OR (od.dateIn BETWEEN :dateIn AND :dateOut) " +
            "    OR (od.dateOut BETWEEN :dateIn AND :dateOut) " +
            "  ) " +
            "  AND o.status = 'BOOKED'" +
            ")")
    List<Room> findAvailableRooms(@Param("dateIn") LocalDateTime dateIn,
                                  @Param("dateOut") LocalDateTime dateOut);

    @Query("SELECT r FROM Room r WHERE r.category.slug = :slug")
    List<Room> findRoomsByCategorySlug(@Param("slug") String slug);

    List<Room> findByCategoryId(Integer categoryId);

    boolean existsByName(String name);

    long countByStatus(boolean status);

    @Query(nativeQuery = true, value = "SELECT * FROM Rooms where status = true")
    List<Room> findAllRoomActive();

    @Query("SELECT COUNT(r) FROM Room r WHERE r.id NOT IN (SELECT od.room.id FROM OrderDetail od WHERE od.dateIn < :dateOut AND od.dateOut > :dateIn)")
    int countAvailableRooms(@Param("dateIn") LocalDateTime dateIn, @Param("dateOut") LocalDateTime dateOut);
}

