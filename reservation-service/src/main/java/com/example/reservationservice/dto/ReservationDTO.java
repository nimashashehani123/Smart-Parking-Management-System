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
    private Long parkingSpaceId;
    private String startTime;
    private String endTime;
    private String status;
}

