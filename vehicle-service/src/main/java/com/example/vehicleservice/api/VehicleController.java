package com.example.vehicleservice.api;

import com.example.vehicleservice.dto.VehicleDTO;
import com.example.vehicleservice.entity.Vehicle;
import com.example.vehicleservice.service.VehicleService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping
    public Vehicle addVehicle(@RequestBody VehicleDTO dto) {
        return vehicleService.addVehicle(dto);
    }

    @GetMapping
    @PreAuthorize("hasRole('OWNER')")
    public List<Vehicle> getAllVehicles(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        System.out.println(authHeader);
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public List<Vehicle> getVehiclesByUser(@PathVariable Long userId) {
        return vehicleService.getVehiclesByUserId(userId);
    }
}
