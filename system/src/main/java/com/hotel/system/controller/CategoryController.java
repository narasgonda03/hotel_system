package com.hotel.system.controller;

import com.hotel.system.entity.Category;
import com.hotel.system.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // Add Category
    @PostMapping("/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addCategory(category));
    }

    // Get All Categories
    @GetMapping("/all")
    public List<Category> getAllCategories() {
        return service.getAllCategories();
    }

    // Update Category
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Long id,
                                            @RequestBody Category category) {
        try {
            return ResponseEntity.ok(service.updateCategory(id, category));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Delete Category
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.deleteCategory(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}