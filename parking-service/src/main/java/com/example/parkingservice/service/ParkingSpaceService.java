package com.example.parkingservice.service;

import com.example.parkingservice.dto.ParkingSpaceDTO;
import com.example.parkingservice.entity.ParkingSpace;

import java.util.List;

public interface ParkingSpaceService {
    ParkingSpace addSpace(ParkingSpaceDTO dto);
    List<ParkingSpace> getAll();
    List<ParkingSpace> getByOwner(Long ownerId);
}
