package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private Long id;
    private Long userId;
    private Long parkingSpaceId;
    private String startTime;
    private String endTime;
    private String status;
}
