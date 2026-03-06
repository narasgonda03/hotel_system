package com.hotel.system.service;

import com.hotel.system.entity.HotelTable;
import com.hotel.system.repository.HotelTableRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HotelTableService {

    private final HotelTableRepository repo;

    public HotelTableService(HotelTableRepository repo) {
        this.repo = repo;
    }

    // Table Add
    public HotelTable addTable(HotelTable table) {
        table.setStatus("AVAILABLE");
        return repo.save(table);
    }

    // सगळे Tables
    public List<HotelTable> getAllTables() {
        return repo.findAll();
    }

    // Available Tables
    public List<HotelTable> getAvailableTables() {
        return repo.findByStatus("AVAILABLE");
    }

    // Table Status Update
    public HotelTable updateStatus(Long id, String status) {
        HotelTable table = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found: " + id));
        table.setStatus(status);
        return repo.save(table);
    }

    // Table Delete
    public String deleteTable(Long id) {
        HotelTable table = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found: " + id));
        if (table.getStatus().equals("OCCUPIED")) {
            throw new RuntimeException("Cannot delete occupied table!");
        }
        repo.deleteById(id);
        return "Table deleted successfully";
    }
}