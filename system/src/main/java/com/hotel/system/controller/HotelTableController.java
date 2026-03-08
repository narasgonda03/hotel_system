package com.hotel.system.controller;

import com.hotel.system.entity.HotelTable;
import com.hotel.system.service.HotelTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tables")
// FIX #5: @CrossOrigin(origins = "*") काढला — CorsConfig.java globally handle करतो
public class HotelTableController {

    @Autowired
    private HotelTableService hotelTableService;

    // GET /tables/all
    @GetMapping("/all")
    public ResponseEntity<List<HotelTable>> getAllTables() {
        return ResponseEntity.ok(hotelTableService.getAllTables());
    }

    // GET /tables/floor/{floorName}
    @GetMapping("/floor/{floorName}")
    public ResponseEntity<List<HotelTable>> getByFloor(@PathVariable String floorName) {
        return ResponseEntity.ok(hotelTableService.getTablesByFloor(floorName));
    }

    // GET /tables/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return hotelTableService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /tables/add
    @PostMapping("/add")
    public ResponseEntity<?> addTable(@RequestBody HotelTable table) {
        try {
            HotelTable saved = hotelTableService.addTable(table);
            return ResponseEntity.status(201).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /tables/update/{id}
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTable(@PathVariable Long id, @RequestBody HotelTable table) {
        try {
            return ResponseEntity.ok(hotelTableService.updateTable(id, table));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // PUT /tables/status/{id}?status=OCCUPIED
    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @RequestParam HotelTable.TableStatus status) {
        try {
            return ResponseEntity.ok(hotelTableService.updateStatus(id, status));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // DELETE /tables/delete/{id}
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTable(@PathVariable Long id) {
        try {
            hotelTableService.deleteTable(id);
            return ResponseEntity.ok("Table deleted successfully!");
        } catch (DataIntegrityViolationException e) {
            // FIX: DB level foreign key constraint — clear message पाठवणे
            return ResponseEntity.badRequest()
                    .body("Cannot delete! This table has linked orders in history.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}