package com.hotelking.controller;

import com.hotelking.entity.Category;
import com.hotelking.entity.Order;
import com.hotelking.repository.CategoryRepository;
import com.hotelking.repository.OrderRepository;
import com.hotelking.service.SlugService;
import com.hotelking.service.category.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin/room-category-management")
public class AdminCategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public String getCategories(Model model) {
        model.addAttribute("categories", categoryService.findAllCategories());
        model.addAttribute("category", new Category());
        return "admin/category";
    }

    private void loadTable(Model model) {
        List<Category> categories = categoryRepository.findAll();
        model.addAttribute("categories", categories);
    }

    @PostMapping("/save")
    public String saveCategory(@ModelAttribute("category") @Valid Category category, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            loadTable(model);
            return "admin/category";
        }

        List<String> errorMessages = new ArrayList<>();

        if (categoryService.existsByName(category.getName())) {
            errorMessages.add("Tên đã tồn tại.");
        }

        if (!errorMessages.isEmpty()) {
            model.addAttribute("errorMessages", errorMessages);
            loadTable(model);
            return "admin/category";
        }

        try {
            categoryService.createOrUpdateCategory(category);
            redirectAttributes.addFlashAttribute("message", "Thêm thành công");
        } catch (Exception e) {
            errorMessages.add("Có lỗi xảy ra trong quá trình lưu.");
            model.addAttribute("errorMessages", errorMessages);
            loadTable(model);
            return "admin/category";
        }

        return "redirect:/admin/room-category-management";
    }

    @GetMapping("/{id}")
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        Category category = categoryService.findById(id).orElse(null);
        if (category != null) {
            categoryService.delete(category);
            redirectAttributes.addFlashAttribute("message", "Xoá thành công!");
        } else {
            redirectAttributes.addFlashAttribute("message", "Không tìm thấy item!");
        }
        return "redirect:/admin/room-category-management";
    }
}
