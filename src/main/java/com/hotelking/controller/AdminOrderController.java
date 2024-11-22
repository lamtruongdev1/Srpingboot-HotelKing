package com.hotelking.controller;

import com.hotelking.entity.Category;
import com.hotelking.entity.Order;
import com.hotelking.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AdminOrderController {

    @Autowired
    OrderRepository orderRepository;

    @PostMapping
    public String save(Model model, @ModelAttribute("order") Order order) {
        orderRepository.save(order);
        model.addAttribute("message", "saved successful!");
        return "admin/order";
    }

    @GetMapping("/{id}")
    public String delete(Model model, @PathVariable("id") Integer id) {
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            orderRepository.delete(order);
            model.addAttribute("message", "deleted successful!");
        } else {
            model.addAttribute("message", "not found this item!");
        }
        return "admin/category";
    }
}
