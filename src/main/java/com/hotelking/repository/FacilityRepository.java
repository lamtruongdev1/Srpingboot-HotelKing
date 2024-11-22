package com.hotelking.repository;

import com.hotelking.entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FacilityRepository extends JpaRepository<Facility, Integer> {
    @Modifying
    @Query("DELETE FROM RoomFacility rf WHERE rf.facility.id = :facilityId")
    void deleteByFacilityId(@Param("facilityId") Integer facilityId);
}
