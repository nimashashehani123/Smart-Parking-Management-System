package com.example.paymentservice.service.impl;

import com.example.parkingservice.dto.ParkingSpaceDTO;
import com.example.paymentservice.dto.ResponseDTO;
import com.example.paymentservice.dto.TransactionDTO;
import com.example.paymentservice.entity.Transaction;
import com.example.paymentservice.repository.TransactionRepository;
import com.example.paymentservice.service.TransactionService;
import com.example.reservationservice.dto.ReservationDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Value("http://localhost:8080/api/v1/reservations/")
    private String reservationServiceUrl;

    @Value("http://localhost:8080/api/v1/parkings/")
    private String parkingServiceUrl;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public Transaction makePayment(TransactionDTO dto) {
        boolean isValidCard = dto.getCardNumber() != null && dto.getCardNumber().matches("\\d{16}");

        Transaction transaction = new Transaction();
        transaction.setReservationId(dto.getReservationId());
        transaction.setUserId(dto.getUserId());

        if (isValidCard) {
            // 🔁 Fetch reservation details via WebClient
            ResponseDTO response = webClientBuilder.build()
                    .get()
                    .uri(reservationServiceUrl + dto.getReservationId())
                    .retrieve()
                    .bodyToMono(ResponseDTO.class)
                    .block();
            ReservationDTO reservation = objectMapper.convertValue(response.getData(), ReservationDTO.class);

            // 🔁 Fetch pricePerHour from ParkingService

            String parkingUrl = parkingServiceUrl + "get/" + reservation.getParkingSpaceId();
            ResponseDTO response1 = webClientBuilder.build()
                    .get()
                    .uri(parkingUrl)
                    .retrieve()
                    .bodyToMono(ResponseDTO.class)
                    .block();

            ParkingSpaceDTO space = objectMapper.convertValue(response1.getData(), ParkingSpaceDTO.class);

            // 🧮 Calculate duration
            LocalDateTime start = LocalDateTime.parse(reservation.getStartTime());
            LocalDateTime end = LocalDateTime.now();
            double hours = Duration.between(start, end).toMinutes() / 60.0;

            // 🧮 Calculate total
            double total = hours * space.getPricePerHour();

            transaction.setStartTime(start);
            transaction.setEndTime(end);
            transaction.setPricePerHour(space.getPricePerHour());

            transaction.setAmount(Math.round(total * 100.0) / 100.0); // round to 2 decimal
            transaction.setStatus("SUCCESS");

            // ✅ Update reservation status to PAID
            webClientBuilder.build()
                    .patch()
                    .uri(reservationServiceUrl + "status/" + dto.getReservationId())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe();

            webClientBuilder.build()
                    .patch()
                    .uri(parkingServiceUrl + "release/" + reservation.getParkingSpaceId())
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe(); // 🔥 fire-and-forget
        } else {
            transaction.setAmount(0.0);
            transaction.setStatus("FAILED");
        }

        return transactionRepository.save(transaction);
    }


    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

}
