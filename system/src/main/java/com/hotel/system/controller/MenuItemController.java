package com.hotel.system.controller;

import com.hotel.system.dto.MenuItemRequest;
import com.hotel.system.entity.MenuItem;
import com.hotel.system.service.MenuItemService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
// FIX #5: @CrossOrigin("*") काढला — CorsConfig.java globally handle करतो
public class MenuItemController {

    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    // ADD MENU ITEM
    @PostMapping("/add")
    public ResponseEntity<MenuItem> addItem(@Valid @RequestBody MenuItemRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addItem(request));
    }

    // GET ALL MENU ITEMS
    @GetMapping("/all")
    public ResponseEntity<List<MenuItem>> getAllItems() {
        return ResponseEntity.ok(service.getAllItems());
    }

    // GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getItemById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getItemById(id));
    }

    // GET BY CATEGORY — category नुसार menu बघणे
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<MenuItem>> getByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(service.getItemsByCategory(categoryId));
    }

    // UPDATE MENU ITEM — नाव/किंमत/availability बदलणे
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id,
                                        @RequestBody MenuItemRequest request) {
        try {
            return ResponseEntity.ok(service.updateItem(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // FIX: TOGGLE AVAILABILITY — /menu/toggle/{id}
    @PutMapping("/toggle/{id}")
    public ResponseEntity<?> toggleAvailable(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.toggleAvailable(id));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE MENU ITEM
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteItem(@PathVariable Long id) {
        return ResponseEntity.ok(service.deleteItem(id));
    }
}