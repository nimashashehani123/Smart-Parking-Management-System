package com.example.parkingservice.api;

import com.example.parkingservice.dto.ParkingSpaceDTO;
import com.example.parkingservice.entity.ParkingSpace;
import com.example.parkingservice.service.ParkingSpaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/parkings")
public class ParkingSpaceController {

    @Autowired
    private ParkingSpaceService service;

    @PostMapping
    public ParkingSpace addSpace(@RequestBody ParkingSpaceDTO dto) {
        return service.addSpace(dto);
    }

    @GetMapping
    public List<ParkingSpace> getAll() {
        return service.getAll();
    }

    @GetMapping("/owner/{ownerId}")
    public List<ParkingSpace> getByOwner(@PathVariable Long ownerId) {
        return service.getByOwner(ownerId);
    }
}
