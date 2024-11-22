package com.hotelking.controller;

import com.hotelking.entity.Category;
import com.hotelking.entity.Facility;
import com.hotelking.entity.Room;
import com.hotelking.repository.CategoryRepository;
import com.hotelking.repository.RoomRepository;
import com.hotelking.service.SlugService;
import com.hotelking.service.category.CategoryService;
import com.hotelking.service.facility.FacilityService;
import com.hotelking.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Controller
@RequestMapping("/admin/room-management")
public class AdminRoomController {

    @Autowired
    RoomService roomService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    FacilityService facilityService;


    @GetMapping
    public String index(Model model) {
        loadTable(model);
        return "admin/room";
    }

    private void loadTable(Model model) {
        List<Room> rooms = roomService.getAllRooms();
        List<Category> categories = roomService.getAllCategories();
        List<Facility> facilities = facilityService.findAll();

        model.addAttribute("facilities", facilities);
        model.addAttribute("rooms", rooms);
        model.addAttribute("categories", categories);
    }

    @PostMapping("/save")
    public String saveRoom(@ModelAttribute("room") Room room,
                           @RequestParam(value = "facilityIds", required = false) List<Integer> facilityIds,
                           @RequestParam(value = "category_id", required = false) Integer categoryId,
                           @RequestParam(value = "status", required = false) Boolean status,
                           @RequestParam(value = "file", required = false) MultipartFile imageFile,
                           RedirectAttributes redirectAttributes) {

        // Kiểm tra room hiện có trong cơ sở dữ liệu
        Room existingRoom = roomService.getRoomById(room.getId()).orElse(null);

        // Nếu room tồn tại, cập nhật các thuộc tính khác ngoài file ảnh
        if (existingRoom != null) {
            room.setSlug(SlugService.toSlug(room.getName()));

            if (categoryId != null) {
                Category category = categoryService.findById(categoryId).orElse(null);
                room.setCategory(category);
            }

            Set<Facility> facilities = new HashSet<>();
            if (facilityIds != null) {
                for (Integer facilityId : facilityIds) {
                    Facility facility = facilityService.findById(facilityId);
                    if (facility != null) {
                        facilities.add(facility);
                    }
                }
            }
            room.setFacilities(facilities);

            // Cập nhật status nếu có, nếu không giữ nguyên giá trị hiện tại
            if (status != null) {
                room.setStatus(status);
            } else {
                room.setStatus(existingRoom.getStatus());
            }

            // Xử lý ảnh nếu có upload ảnh mới
            if (!imageFile.isEmpty()) {
                try {
                    String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                    Path uploadPath = Paths.get("uploads/images/rooms/");

                    // Tạo thư mục nếu chưa tồn tại
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    // Xóa ảnh cũ nếu có
                    if (existingRoom.getImage() != null) {
                        Path oldImagePath = uploadPath.resolve(existingRoom.getImage());
                        if (Files.exists(oldImagePath)) {
                            Files.delete(oldImagePath);
                        }
                    }

                    // Lưu ảnh mới
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    room.setImage(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload file: " + e.getMessage());
                    return "redirect:/admin/room-management";
                }
            } else {
                // Nếu không có ảnh mới thì giữ nguyên ảnh cũ
                room.setImage(existingRoom.getImage());
            }
        } else {
            // Trường hợp tạo mới room
            room.setSlug(SlugService.toSlug(room.getName()));

            if (categoryId != null) {
                Category category = categoryService.findById(categoryId).orElse(null);
                room.setCategory(category);
            }

            Set<Facility> facilities = new HashSet<>();
            if (facilityIds != null) {
                for (Integer facilityId : facilityIds) {
                    Facility facility = facilityService.findById(facilityId);
                    if (facility != null) {
                        facilities.add(facility);
                    }
                }
            }
            room.setFacilities(facilities);
            room.setStatus(status);

            // Xử lý ảnh nếu có upload ảnh
            if (!imageFile.isEmpty()) {
                try {
                    String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
                    Path uploadPath = Paths.get("uploads/images/rooms/");

                    // Tạo thư mục nếu chưa tồn tại
                    if (!Files.exists(uploadPath)) {
                        Files.createDirectories(uploadPath);
                    }

                    // Lưu ảnh mới
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    room.setImage(fileName);
                } catch (IOException e) {
                    e.printStackTrace();
                    redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload file: " + e.getMessage());
                    return "redirect:/admin/room-management";
                }
            }
        }

        // Lưu hoặc cập nhật room
        roomService.saveOrUpdateRoom(room);

        redirectAttributes.addFlashAttribute("message", "Cập nhật thành công!");
        return "redirect:/admin/room-management";
    }



    @GetMapping("/{id}")
    public String delete(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Integer id) {
        Room room = roomService.getRoomById(id).orElse(null);
        if (room != null) {
            roomService.deleteRoomById(id);
            redirectAttributes.addFlashAttribute("message", "Xoá thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy item!");
        }
        loadTable(model);
        return "redirect:/admin/room-management";
    }
}
