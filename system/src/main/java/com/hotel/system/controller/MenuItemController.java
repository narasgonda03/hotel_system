package com.hotel.system.controller;

import com.hotel.system.entity.MenuItem;
import com.hotel.system.service.MenuItemService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/menu")
@CrossOrigin("*")
public class MenuItemController {

    private final MenuItemService service;

    public MenuItemController(MenuItemService service) {
        this.service = service;
    }

    @PostMapping("/add")
    public MenuItem addItem(@RequestBody MenuItem item) {
        return service.addItem(item);
    }

    @GetMapping("/all")
    public List<MenuItem> getItems() {
        return service.getAllItems();
    }
}
