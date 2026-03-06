package com.hotel.system.repository;

import com.hotel.system.entity.HotelTable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HotelTableRepository extends JpaRepository<HotelTable, Long> {
    List<HotelTable> findByStatus(String status);
}