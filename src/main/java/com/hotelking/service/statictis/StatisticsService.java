package com.hotelking.service.statictis;

import com.hotelking.repository.OrderRepository;
import com.hotelking.repository.RoomRepository;
import com.hotelking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class StatisticsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private OrderRepository orderRepository;

    public long getTotalUsers() {
        return userRepository.count();
    }

    public long getTotalOrders() {
        return orderRepository.count();
    }
    public long getTotalRoomsActive() {
        return roomRepository.countByStatus(true);
    }

    public double getActiveRoomPercentage() {
        long totalRooms = roomRepository.count();
        long activeRooms = roomRepository.countByStatus(true);
        return (double) activeRooms / totalRooms * 100;
    }

    public List<Double> getMonthlyRevenueForCurrentYear() {
        List<Double> monthlyRevenue = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        int currentYear = now.getYear();

        for (int month = 1; month <= 12; month++) {
            YearMonth yearMonth = YearMonth.of(currentYear, month);
            LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
            LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

            Double revenue = orderRepository.sumTotalAmountByDateRange(startOfMonth, endOfMonth);
            monthlyRevenue.add(revenue != null ? revenue : 0.0);
        }

        return monthlyRevenue;
    }

    public double getCurrentMonthRevenue() {
        LocalDateTime now = LocalDateTime.now();
        YearMonth yearMonth = YearMonth.of(now.getYear(), now.getMonth());
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        Double revenue = orderRepository.sumTotalAmountByDateRange(startOfMonth, endOfMonth);
        return revenue != null ? revenue : 0.0;
    }
}