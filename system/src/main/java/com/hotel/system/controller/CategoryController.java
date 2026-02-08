package com.hotel.system.controller;

import com.hotel.system.entity.Category;
import com.hotel.system.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@CrossOrigin("*")
public class CategoryController {

    private final CategoryService service;

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // Add category
    @PostMapping("/add")
    public Category addCategory(@RequestBody Category category) {
        return service.addCategory(category);
    }

    // Get all categories
    @GetMapping("/all")
    public List<Category> getAllCategories() {
        return service.getAllCategories();
    }
}
