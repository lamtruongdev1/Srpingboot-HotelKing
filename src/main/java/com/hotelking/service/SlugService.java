package com.hotelking.service;

import com.hotelking.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.regex.Pattern;

@Service
public class SlugService {

    private static CategoryRepository categoryRepository;

    @Autowired
    public SlugService(CategoryRepository categoryRepository) {
        SlugService.categoryRepository = categoryRepository;
    }

    public static String toSlug(String input) {
        String nonLatin = "[^\\w-]";
        Pattern pattern = Pattern.compile(nonLatin);

        String nowhitespace = input.trim().replaceAll("\\s+", "-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = pattern.matcher(normalized).replaceAll("").toLowerCase();

        return makeSlugUnique(slug);
    }

    private static String makeSlugUnique(String slug) {
        String uniqueSlug = slug;
        int counter = 1;
        while (categoryRepository.existsBySlug(uniqueSlug)) {
            uniqueSlug = slug + "-" + counter++;
        }
        return uniqueSlug;
    }
}
