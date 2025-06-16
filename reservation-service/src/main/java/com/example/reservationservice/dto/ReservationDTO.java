package com.example.reservationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReservationDTO {
    private Long userId;
    private Long vehicleId;
    private Long parkingId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;

    // Getters and setters
}
