package com.hotelking.api;

import com.hotelking.dto.BookRoomRequest;
import com.hotelking.dto.CheckRoomRequest;
import com.hotelking.entity.*;
import com.hotelking.repository.*;
import com.hotelking.service.category.CategoryService;
import com.hotelking.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/rooms")
public class RoomRestController {

    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomService roomService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        List<Room> rooms = roomService.getAllRoomsActive();
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/list-categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.findAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @GetMapping("/list-by-category/{slug}")
    public ResponseEntity<List<Room>> getRoomsByCategory(@PathVariable String slug) {
        List<Room> rooms = roomService.getRoomsByCategorySlug(slug);
        return new ResponseEntity<>(rooms, HttpStatus.OK);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Room> getRoomBySlug(@PathVariable String slug) {
        Optional<Room> room = roomRepository.findBySlug(slug);
        return room.map(r -> new ResponseEntity<>(r, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        Room savedRoom = roomRepository.save(room);
        return new ResponseEntity<>(savedRoom, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Room> updateRoom(@PathVariable int id, @RequestBody Room room) {
        if (roomRepository.existsById(id)) {
            room.setId(id);
            Room updatedRoom = roomRepository.save(room);
            return new ResponseEntity<>(updatedRoom, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable int id) {
        if (roomRepository.existsById(id)) {
            roomRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/check-room")
    public ResponseEntity<Map<String, Boolean>> checkRoom(@RequestBody CheckRoomRequest request) {
        LocalDateTime dateIn = request.getDateIn().atStartOfDay();
        LocalDateTime dateOut = request.getDateOut().atStartOfDay().plusDays(1);

        boolean isAvailable = roomService.checkRoomAvailability(dateIn, dateOut, request.getNumberOfRooms());

        Map<String, Boolean> response = new HashMap<>();
        response.put("isAvailable", isAvailable);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PostMapping("/book-room")
    public ResponseEntity<String> bookRoom(@RequestBody BookRoomRequest request) {
        try {
            Customer customer = new Customer();
            customer.setFullname(request.getCustomerName());
            customer.setPhone(request.getCustomerPhone());
            customer.setEmail(request.getCustomerEmail());
            customer.setAddress(request.getCustomerAddress());
            Customer savedCustomer = customerRepository.save(customer);

            // Lấy thông tin thanh toán
            Payment payment = paymentRepository.findById(request.getPaymentId()).orElse(null);

            // Chuyển đổi thời gian nhận phòng và trả phòng
            LocalDateTime dateIn = convertToLocalDateTime(request.getDateIn());
            LocalDateTime dateOut = convertToLocalDateTime(request.getDateOut()).plusDays(1);

            // Đặt phòng
            boolean isBooked = roomService.bookRoom(
                    dateIn,
                    dateOut,
                    request.getAdultGuests(),
                    request.getChildGuests(),
                    request.getNumberOfRooms(),
                    savedCustomer,
                    payment
            );

            if (isBooked) {
                return new ResponseEntity<>("Đặt phòng thành công!", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Không thể đặt phòng, vui lòng thử lại.", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            // Ghi log lỗi để phục vụ debug
            e.printStackTrace();
            return new ResponseEntity<>("Đã xảy ra lỗi khi đặt phòng.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Phương thức chuyển đổi từ Date sang LocalDateTime
    private LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}
