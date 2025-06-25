package com.example.parkingservice.service.impl;

import com.example.parkingservice.dto.ParkingSpaceDTO;
import com.example.parkingservice.entity.ParkingSpace;
import com.example.parkingservice.repository.ParkingSpaceRepository;
import com.example.parkingservice.service.ParkingSpaceService;
import com.example.parkingservice.utill.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    @Autowired
    private ParkingSpaceRepository repo;

    @Override
    public int addParkingSpace(ParkingSpaceDTO dto) {
        ParkingSpace p = new ParkingSpace();
        p.setLocation(dto.getLocation());
        p.setStatus(dto.getStatus());
        p.setZone(dto.getZone());
        p.setOwnerId(dto.getOwnerId());
        p.setPricePerHour(dto.getPricePerHour());
        repo.save(p);
        return VarList.Created;
    }

    @Override
    public int updateParkingSpace(Long id,ParkingSpaceDTO dto) {
        Optional<ParkingSpace> optional = repo.findById(id);
        if (optional.isPresent()) {
            ParkingSpace p = optional.get();
            p.setLocation(dto.getLocation());
            p.setZone(dto.getZone());
            p.setStatus(dto.getStatus());
            p.setOwnerId(dto.getOwnerId());
            p.setPricePerHour(dto.getPricePerHour());
            repo.save(p);
            return VarList.OK;
        } else {
            return VarList.Not_Acceptable;
        }
    }

    @Override
    public int changeStatus(Long id, String status) {
        Optional<ParkingSpace> optional = repo.findById(id);
        if (optional.isPresent()) {
            ParkingSpace p = optional.get();
            p.setStatus(status.toUpperCase());
            repo.save(p);
            return VarList.OK;
        }
        return VarList.Not_Acceptable;
    }

    @Override
    public List<ParkingSpaceDTO> getByZone(String zone) {
        return repo.findByZone(zone).stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public ParkingSpaceDTO getById(Long id) {
        Optional<ParkingSpace> space = repo.findById(id);
        return space.map(this::toDTO).orElse(null);
    }


    @Override
    public List<ParkingSpaceDTO> getAvailableSpaces() {
        return repo.findByStatus("AVAILABLE").stream().map(this::toDTO).collect(Collectors.toList());
    }

    private ParkingSpaceDTO toDTO(ParkingSpace p) {
        ParkingSpaceDTO dto = new ParkingSpaceDTO();
        dto.setLocation(p.getLocation());
        dto.setZone(p.getZone());
        dto.setStatus(p.getStatus());
        dto.setOwnerId(p.getOwnerId());
        dto.setPricePerHour(p.getPricePerHour());
        return dto;
    }
}

