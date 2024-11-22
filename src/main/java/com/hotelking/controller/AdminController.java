package com.hotelking.controller;

import com.hotelking.service.statictis.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Controller
public class AdminController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping({"/admin", "/admin/", "/admin/dashboard"})
    public String index(Model model) {
        long totalUsers = statisticsService.getTotalUsers();
        long totalOrders = statisticsService.getTotalOrders();
        long totalRooms = statisticsService.getTotalRoomsActive();
        double activeRoomPercentage = statisticsService.getActiveRoomPercentage();
        double currentMonthRevenue = statisticsService.getCurrentMonthRevenue();
//        List<Double> monthlyRevenue = statisticsService.getMonthlyRevenueForYear(LocalDate.now().getYear());

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("totalRooms", totalRooms);
        model.addAttribute("activeRoomPercentage", String.format("%.2f", activeRoomPercentage));
        model.addAttribute("currentMonthRevenue", currentMonthRevenue);
//        model.addAttribute("monthlyRevenue", monthlyRevenue);

        return "admin/dashboard";
    }

}
