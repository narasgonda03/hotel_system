package com.hotel.system.service;

import com.hotel.system.entity.Category;
import com.hotel.system.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    // Add Category
    public Category addCategory(Category category) {
        return repo.save(category);
    }

    // Get All Categories
    public List<Category> getAllCategories() {
        return repo.findAll();
    }

    // Update Category — नाव बदलणे
    public Category updateCategory(Long id, Category updatedData) {
        Category existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (updatedData.getName() != null) {
            existing.setName(updatedData.getName());
        }

        return repo.save(existing);
    }

    // Delete Category
    public String deleteCategory(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Category not found with id: " + id);
        }
        repo.deleteById(id);
        return "Category deleted successfully";
    }
}