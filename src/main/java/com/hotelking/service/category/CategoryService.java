package com.hotelking.service.category;

import com.hotelking.entity.Category;
import com.hotelking.repository.CategoryRepository;
import com.hotelking.service.SlugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public void createOrUpdateCategory(Category category) {
        category.setSlug(SlugService.toSlug(category.getName()));
        categoryRepository.save(category);
    }

    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    public void delete(Category category) {
        categoryRepository.delete(category);
    }

    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }

    public Optional<Category> findById(Integer id) {
        return categoryRepository.findById(id);
    }
}
