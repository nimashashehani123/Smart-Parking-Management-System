package com.example.vehicleservice.service.impl;

import com.example.vehicleservice.dto.VehicleDTO;
import com.example.vehicleservice.entity.Vehicle;
import com.example.vehicleservice.repository.VehicleRepository;
import com.example.vehicleservice.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public Vehicle addVehicle(VehicleDTO dto) {
        Vehicle vehicle = new Vehicle();
        vehicle.setUserId(dto.getUserId());
        vehicle.setPlateNumber(dto.getPlateNumber());
        vehicle.setModel(dto.getModel());
        vehicle.setColor(dto.getColor());
        return vehicleRepository.save(vehicle);
    }

    @Override
    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    @Override
    public List<Vehicle> getVehiclesByUserId(Long userId) {
        return vehicleRepository.findByUserId(userId);
    }

    @Override
    public Vehicle updateVehicle(Long id, VehicleDTO dto) {
        Vehicle v = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found"));
        v.setPlateNumber(dto.getPlateNumber());
        v.setColor(dto.getColor());
        v.setModel(dto.getModel());
        v.setUserId(dto.getUserId());
        return vehicleRepository.save(v);
    }

    @Override
    public Vehicle getVehicleById(Long id) {
        return vehicleRepository.findById(id).orElse(null);
    }



}
