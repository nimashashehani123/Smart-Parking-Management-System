package com.example.parkingservice.service;

import com.example.parkingservice.dto.ParkingSpaceDTO;
import com.example.parkingservice.entity.ParkingSpace;

import java.util.List;

import com.example.parkingservice.dto.ParkingSpaceDTO;

import java.util.List;

public interface ParkingSpaceService {
    int addParkingSpace(ParkingSpaceDTO dto);
    int updateParkingSpace(Long id,ParkingSpaceDTO dto);
    int changeStatus(Long id, String status);
    List<ParkingSpaceDTO> getByZone(String zone);
    List<ParkingSpaceDTO> getAvailableSpaces();
    ParkingSpaceDTO getById(Long id);

}
