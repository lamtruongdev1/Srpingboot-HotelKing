package com.hotelking.service.room;

import com.hotelking.entity.*;
import com.hotelking.repository.CategoryRepository;
import com.hotelking.repository.OrderDetailRepository;
import com.hotelking.repository.OrderRepository;
import com.hotelking.repository.RoomRepository;
import com.hotelking.service.facility.FacilityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RoomService {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private FacilityService facilityService;

    public boolean checkRoomAvailability(LocalDateTime checkInDate, LocalDateTime checkOutDate, int numberOfRooms) {
        List<Room> availableRooms = roomRepository.findAvailableRooms(checkInDate, checkOutDate);
        return availableRooms.size() >= numberOfRooms;
    }

    private double calculateTotalAmount(List<Room> rooms) {
        return rooms.stream()
                .mapToDouble(Room::getPrice)
                .sum();
    }


    @Transactional
    public boolean bookRoom(LocalDateTime dateIn, LocalDateTime dateOut, int adultGuests, int childGuests, int numberOfRooms, Customer customer, Payment payment) {
        // Kiểm tra tính khả dụng của phòng
        if (!checkRoomAvailability(dateIn, dateOut, numberOfRooms)) {
            return false;
        }

        // Lấy danh sách phòng có sẵn trong khoảng thời gian đã chọn
        List<Room> availableRooms = roomRepository.findAvailableRooms(dateIn, dateOut);
        if (availableRooms.size() < numberOfRooms) {
            return false;
        }

        // Chọn các phòng để đặt
        List<Room> roomsToBook = availableRooms.subList(0, numberOfRooms);

        // Tạo và lưu đơn hàng
        Order order = new Order();
        order.setTotalAmount(calculateTotalAmount(roomsToBook));
        order.setTotalDateTime(dateIn);
        order.setTotalPerson(adultGuests + childGuests);
        order.setStatus("BOOKED");
        order.setCustomer(customer);
        Order savedOrder = orderRepository.save(order);

        // Tạo và lưu chi tiết đơn hàng
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (Room room : roomsToBook) {
            OrderDetail detail = new OrderDetail();
            detail.setOrder(savedOrder);
            detail.setRoom(room);
            detail.setPayment(payment);
            detail.setDateIn(dateIn);
            detail.setDateOut(dateOut);
            orderDetails.add(detail);
        }
        orderDetailRepository.saveAll(orderDetails);

        return true;
    }

    @Transactional
    public Room saveOrUpdateRoom(Room room) {
        Set<Facility> facilities = room.getFacilities();
        if (facilities != null && !facilities.isEmpty()) {
            for (Facility facility : facilities) {
                Facility existingFacility = facilityService.findById(facility.getId());
                if (existingFacility != null) {
                    facilities.add(existingFacility);
                }
            }
        }

        room.setFacilities(facilities);

        return roomRepository.save(room);
    }

    public List<Room> getRoomsByCategorySlug(String slug) {
        return roomRepository.findRoomsByCategorySlug(slug);
    }

    public Optional<Room> getRoomById(Integer id) {
        return roomRepository.findById(id);
    }

    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    public List<Room> getAllRoomsActive() {
        return roomRepository.findAllRoomActive();
    }

    public void deleteRoomById(Integer id) {
        roomRepository.deleteById(id);
    }

    public List<Room> getRoomsByCategory(Integer categoryId) {
        return roomRepository.findByCategoryId(categoryId);
    }

    public Category saveOrUpdateCategory(Category category) {
        return categoryRepository.save(category);
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public void deleteCategoryById(Integer id) {
        categoryRepository.deleteById(id);
    }

    public boolean existsRoomByName(String name) {
        return roomRepository.existsByName(name);
    }

    public boolean existsCategoryByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
