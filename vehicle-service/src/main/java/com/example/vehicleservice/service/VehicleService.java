package com.example.vehicleservice.service;

import com.example.vehicleservice.dto.VehicleDTO;
import com.example.vehicleservice.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle addVehicle(VehicleDTO dto);
    List<Vehicle> getAllVehicles();
    List<Vehicle> getVehiclesByUserId(Long userId);

}
