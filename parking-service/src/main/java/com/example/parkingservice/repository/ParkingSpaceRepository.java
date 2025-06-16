package com.example.parkingservice.repository;

import com.example.parkingservice.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    List<ParkingSpace> findByOwnerId(Long ownerId);
    List<ParkingSpace> findByStatus(String status);
}
