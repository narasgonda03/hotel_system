package com.hotel.system.repository;

import com.hotel.system.entity.HotelTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelTableRepository extends JpaRepository<HotelTable, Long> {

    // Unique check — same tableNumber on same floor
    boolean existsByTableNumberAndFloor(Integer tableNumber, String floor);

    // Get all tables on a specific floor
    List<HotelTable> findByFloor(String floor);

    // Get tables by status
    List<HotelTable> findByStatus(HotelTable.TableStatus status);

    // Get tables by floor ordered by table number
    List<HotelTable> findByFloorOrderByTableNumberAsc(String floor);
}