package com.hotelking.service.facility;

import com.hotelking.entity.Facility;
import com.hotelking.entity.Room;
import com.hotelking.entity.RoomFacilityId;
import com.hotelking.repository.FacilityRepository;
import com.hotelking.repository.RoomRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FacilityService {

    @Autowired
    FacilityRepository facilityRepository;

    @Autowired
    RoomRepository roomRepository;

    public Facility findById(Integer id) {
        return facilityRepository.findById(id).orElse(null);
    }

    public List<Facility> findAll() {

        return facilityRepository.findAll();
    }

    @Transactional
    public Facility saveOrUpdate(Facility facility) {
        return facilityRepository.save(facility);
    }

    @Transactional
    public void deleteById(Integer id) {
        facilityRepository.deleteByFacilityId(id);

        // Xóa facility
        facilityRepository.deleteById(id);
    }

    public boolean existsById(Integer id) {
        return facilityRepository.existsById(id);
    }

}
