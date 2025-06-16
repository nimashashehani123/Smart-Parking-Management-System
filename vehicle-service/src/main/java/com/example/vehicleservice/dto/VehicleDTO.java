package com.example.vehicleservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDTO {
    private Long userId;
    private String plateNumber;
    private String model;
    private String color;
    // Getters and setters
}
