package com.example.parkingservice.repository;

import com.example.parkingservice.entity.ParkingSpace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace, Long> {
    List<ParkingSpace> findByStatus(String status);
    List<ParkingSpace> findByZone(String zone);
    List<ParkingSpace> findByStatusAndZone(String status, String zone);
}
