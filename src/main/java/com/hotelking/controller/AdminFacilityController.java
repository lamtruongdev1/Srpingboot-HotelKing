package com.hotelking.controller;

import com.hotelking.entity.Category;
import com.hotelking.entity.Facility;
import com.hotelking.entity.Room;
import com.hotelking.service.facility.FacilityService;
import com.hotelking.service.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/facility-management")
public class AdminFacilityController {
    @Autowired
    private RoomService roomService;

    @Autowired
    private FacilityService facilityService;

    private void loadTable(Model model) {
        List<Facility> facilities = facilityService.findAll();
        List<Room> rooms = roomService.getAllRooms();
        model.addAttribute("facilities", facilities);
        model.addAttribute("rooms", rooms);
    }

    @GetMapping
    public String index(Model model) {
        loadTable(model);
        return "admin/facility";
    }

    @PostMapping("/save")
    public String saveRoom(@ModelAttribute("facility") Facility facility,
                           @RequestParam(value = "room_id", required = false) Integer roomId,
                           RedirectAttributes redirectAttributes) {
//        if (roomId != null) {
//            Room room = roomService.getRoomById(roomId).orElse(null);
//            facility.set(room);
//        }

        facilityService.saveOrUpdate(facility);
        redirectAttributes.addFlashAttribute("message", "Thao tác thành công!");

        return "redirect:/admin/facility-management";
    }

    @GetMapping("/{id}")
    public String delete(Model model, RedirectAttributes redirectAttributes, @PathVariable("id") Integer id) {
        Facility facility = facilityService.findById(id);
        if (facility != null) {
            facilityService.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Xoá thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy item!");
        }
        loadTable(model);
        return "redirect:/admin/facility-management";
    }
}
