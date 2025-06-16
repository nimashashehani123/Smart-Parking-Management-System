package com.example.parkingservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ParkingSpaceDTO {
    private Long ownerId;
    private String location;
    private String status;
    private String zone;
    private Double pricePerHour;

    // Getters and setters
}
