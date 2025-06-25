package com.example.paymentservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long reservationId;
    private Long userId;
    private Double amount;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double pricePerHour;
    private LocalDateTime timestamp = LocalDateTime.now();
}

