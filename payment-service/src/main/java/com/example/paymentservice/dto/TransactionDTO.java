package com.example.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private Long reservationId;
    private Long userId;
    private Double amount;
    private String status;
    private String cardNumber; // Simulated input
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double pricePerHour;

}
