package com.example.parkingservice.service.impl;

import com.example.parkingservice.dto.ParkingSpaceDTO;
import com.example.parkingservice.entity.ParkingSpace;
import com.example.parkingservice.repository.ParkingSpaceRepository;
import com.example.parkingservice.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    @Autowired
    private ParkingSpaceRepository repository;

    @Override
    public ParkingSpace addSpace(ParkingSpaceDTO dto) {
        ParkingSpace space = new ParkingSpace();
        space.setOwnerId(dto.getOwnerId());
        space.setLocation(dto.getLocation());
        space.setStatus(dto.getStatus());
        space.setZone(dto.getZone());
        space.setPricePerHour(dto.getPricePerHour());
        return repository.save(space);
    }

    @Override
    public List<ParkingSpace> getAll() {
        return repository.findAll();
    }

    @Override
    public List<ParkingSpace> getByOwner(Long ownerId) {
        return repository.findByOwnerId(ownerId);
    }
}
