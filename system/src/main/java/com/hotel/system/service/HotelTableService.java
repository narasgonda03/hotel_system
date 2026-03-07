package com.hotel.system.service;

import com.hotel.system.entity.HotelTable;
import com.hotel.system.repository.HotelTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HotelTableService {

    @Autowired
    private HotelTableRepository hotelTableRepository;

    // Add table — unique check per floor
    public HotelTable addTable(HotelTable table) {
        boolean exists = hotelTableRepository
                .existsByTableNumberAndFloor(table.getTableNumber(), table.getFloor());
        if (exists) {
            throw new RuntimeException(
                    "Table " + table.getTableNumber() + " already exists on " + table.getFloor() + "!"
            );
        }
        return hotelTableRepository.save(table);
    }

    // Get all tables
    public List<HotelTable> getAllTables() {
        return hotelTableRepository.findAll();
    }

    // Get tables by floor
    public List<HotelTable> getTablesByFloor(String floor) {
        return hotelTableRepository.findByFloorOrderByTableNumberAsc(floor);
    }

    // Get table by id
    public Optional<HotelTable> getById(Long id) {
        return hotelTableRepository.findById(id);
    }

    // Update table
    public HotelTable updateTable(Long id, HotelTable updated) {
        HotelTable existing = hotelTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));

        // Check duplicate only if tableNumber or floor changed
        boolean numberChanged = !existing.getTableNumber().equals(updated.getTableNumber());
        boolean floorChanged  = !existing.getFloor().equals(updated.getFloor());

        if (numberChanged || floorChanged) {
            boolean exists = hotelTableRepository
                    .existsByTableNumberAndFloor(updated.getTableNumber(), updated.getFloor());
            if (exists) {
                throw new RuntimeException(
                        "Table " + updated.getTableNumber() + " already exists on " + updated.getFloor() + "!"
                );
            }
        }

        existing.setTableNumber(updated.getTableNumber());
        existing.setCapacity(updated.getCapacity());
        existing.setFloor(updated.getFloor());
        return hotelTableRepository.save(existing);
    }

    // Update status (AVAILABLE / OCCUPIED)
    public HotelTable updateStatus(Long id, HotelTable.TableStatus status) {
        HotelTable table = hotelTableRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Table not found with id: " + id));
        table.setStatus(status);
        return hotelTableRepository.save(table);
    }

    // Delete table
    public void deleteTable(Long id) {
        if (!hotelTableRepository.existsById(id)) {
            throw new RuntimeException("Table not found with id: " + id);
        }
        hotelTableRepository.deleteById(id);
    }
}