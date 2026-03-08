package com.hotel.system.service;

import com.hotel.system.dto.MenuItemRequest;
import com.hotel.system.entity.Category;
import com.hotel.system.entity.MenuItem;
import com.hotel.system.exception.ResourceNotFoundException;
import com.hotel.system.repository.CategoryRepository;
import com.hotel.system.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository menuRepo;
    private final CategoryRepository categoryRepo;

    public MenuItemService(MenuItemRepository menuRepo, CategoryRepository categoryRepo) {
        this.menuRepo = menuRepo;
        this.categoryRepo = categoryRepo;
    }

    // ADD MENU ITEM
    public MenuItem addItem(MenuItemRequest request) {

        if (request.getCategoryId() == null) {
            throw new RuntimeException("CategoryId must not be null");
        }

        Category category = categoryRepo.findById(request.getCategoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Category not found with id " + request.getCategoryId())
                );

        MenuItem item = new MenuItem();
        item.setName(request.getName());
        item.setPrice(request.getPrice());
        item.setAvailable(request.getAvailable());
        item.setCategory(category);

        return menuRepo.save(item);
    }

    // GET ALL ITEMS
    public List<MenuItem> getAllItems() {
        return menuRepo.findAll();
    }

    // GET BY ID
    public MenuItem getItemById(Long id) {
        return menuRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id: " + id));
    }

    // GET BY CATEGORY
    public List<MenuItem> getItemsByCategory(Long categoryId) {
        return menuRepo.findByCategoryId(categoryId);
    }

    // UPDATE MENU ITEM — नाव, किंमत, availability बदलणे
    public MenuItem updateItem(Long id, MenuItemRequest request) {

        MenuItem item = menuRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id: " + id));

        // फक्त जे दिलं ते update होईल
        if (request.getName() != null) item.setName(request.getName());
        if (request.getPrice() > 0) item.setPrice(request.getPrice());
        if (request.getAvailable() != null) item.setAvailable(request.getAvailable());

        // Category बदलायची असेल तर
        if (request.getCategoryId() != null) {
            Category category = categoryRepo.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.getCategoryId()));
            item.setCategory(category);
        }

        return menuRepo.save(item);
    }

    // FIX: TOGGLE AVAILABILITY — Available to Unavailable and vice versa
    public MenuItem toggleAvailable(Long id) {
        MenuItem item = menuRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id: " + id));
        item.setAvailable(!item.isAvailable());
        return menuRepo.save(item);
    }

    // DELETE MENU ITEM
    public String deleteItem(Long id) {
        if (!menuRepo.existsById(id)) {
            throw new ResourceNotFoundException("MenuItem not found with id: " + id);
        }
        menuRepo.deleteById(id);
        return "MenuItem deleted successfully";
    }
}