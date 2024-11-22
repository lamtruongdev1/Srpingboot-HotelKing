package com.hotelking.api;

import com.hotelking.service.statictis.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/monthly-revenue")
    public List<Double> getMonthlyRevenueForCurrentYear() {
        return statisticsService.getMonthlyRevenueForCurrentYear();
    }
}

