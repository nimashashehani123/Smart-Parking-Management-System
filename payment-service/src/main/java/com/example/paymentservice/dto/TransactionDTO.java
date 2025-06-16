package com.example.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDTO {
    private Long userId;
    private Long reservationId;
    private Double amount;
    private String status;

    // Getters and setters
}
