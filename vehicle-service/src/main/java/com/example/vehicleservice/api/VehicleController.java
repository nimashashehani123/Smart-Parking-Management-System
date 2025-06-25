package com.example.vehicleservice.api;

import com.example.vehicleservice.dto.ResponseDTO;
import com.example.vehicleservice.dto.VehicleDTO;
import com.example.vehicleservice.entity.Vehicle;
import com.example.vehicleservice.service.VehicleService;
import com.example.vehicleservice.utill.VarList;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/vehicles")
public class VehicleController {

    @Autowired
    private VehicleService vehicleService;

    @PostMapping("/add")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> addVehicle(@RequestBody VehicleDTO dto) {
        try {
            Vehicle vehicle = vehicleService.addVehicle(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ResponseDTO(VarList.Created, "Vehicle Added", vehicle));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> getAllVehicles() {
        try {
            List<Vehicle> vehicles = vehicleService.getAllVehicles();
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "All Vehicles", vehicles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> getVehiclesByUser(@PathVariable Long userId) {
        try {
            List<Vehicle> vehicles = vehicleService.getVehiclesByUserId(userId);
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Vehicles by User", vehicles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> updateVehicle(@PathVariable Long id, @RequestBody VehicleDTO dto) {
        try {
            Vehicle updated = vehicleService.updateVehicle(id, dto);
            if (updated == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Vehicle not found", null));
            }
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Vehicle Updated", updated));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> getVehicleById(@PathVariable Long id) {
        try {
            Vehicle vehicle = vehicleService.getVehicleById(id);
            if (vehicle == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Vehicle not found", null));
            }
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Vehicle Found", vehicle));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, e.getMessage(), null));
        }
    }
}
