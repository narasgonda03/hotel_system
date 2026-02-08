package com.hotel.system.service;

import com.hotel.system.entity.MenuItem;
import com.hotel.system.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {

    private final MenuItemRepository repo;

    public MenuItemService(MenuItemRepository repo) {
        this.repo = repo;
    }

    public MenuItem addItem(MenuItem item) {
        return repo.save(item);
    }

    public List<MenuItem> getAllItems() {
        return repo.findAll();
    }
}
