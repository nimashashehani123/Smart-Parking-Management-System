package com.example.reservationservice.service.impl;

import com.example.parkingservice.dto.ParkingSpaceDTO;
import com.example.reservationservice.dto.ReservationDTO;
import com.example.reservationservice.dto.ResponseDTO;
import com.example.reservationservice.entity.Reservation;
import com.example.reservationservice.repository.ReservationRepository;
import com.example.reservationservice.service.ReservationService;
import com.example.reservationservice.utill.VarList;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository reservationRepo;

    @Autowired
    private WebClient.Builder webClientBuilder;
    @Value("http://localhost:8080/api/v1/parkings/")
    private String parkingServiceUrl;

    @Autowired
    private ObjectMapper objectMapper;



    @Override
    public int createReservation(ReservationDTO dto) {
        // Step 1: Get parking space details
        String url = parkingServiceUrl + "get/" + dto.getParkingSpaceId();
        ResponseDTO response = webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(ResponseDTO.class)
                .block(); // wait for response

        ParkingSpaceDTO parkingSpace = objectMapper.convertValue(response.getData(), ParkingSpaceDTO.class);

        // Step 2: Check if status is AVAILABLE
        if (parkingSpace == null || !parkingSpace.getStatus().equalsIgnoreCase("AVAILABLE")) {
            return VarList.Not_Acceptable; // status NOT AVAILABLE
        }

        Reservation reservation = new Reservation();
        reservation.setUserId(dto.getUserId());
        reservation.setParkingSpaceId(dto.getParkingSpaceId());
        reservation.setStartTime(LocalDateTime.now());
        reservation.setEndTime(LocalDateTime.now());
        reservation.setStatus("ACTIVE");

        reservationRepo.save(reservation); // ✅ Reservation saved
        webClientBuilder.build()
                .patch()
                .uri(parkingServiceUrl + "reserve/" + dto.getParkingSpaceId())
                .retrieve()
                .bodyToMono(Void.class)
                .subscribe(); // 🔥 fire-and-forget

        return VarList.Created;
    }


    @Override
    public int cancelReservation(Long id) {
        Optional<Reservation> optional = reservationRepo.findById(id);
        if (optional.isPresent()) {
            Reservation r = optional.get();
            r.setStatus("CANCELLED");
            reservationRepo.save(r);
            return VarList.OK;
        } else {
            return VarList.Not_Acceptable;
        }
    }

    @Override
    public ReservationDTO getReservation(Long id) {
        return reservationRepo.findById(id)
                .map(r -> new ReservationDTO(
                        r.getUserId(), r.getParkingSpaceId(),
                        r.getStartTime().toString(), r.getEndTime().toString(), r.getStatus()))
                .orElse(null);
    }

    @Override
    public List<ReservationDTO> getReservationsByUser(Long userId) {
        return reservationRepo.findByUserId(userId).stream().map(r ->
                new ReservationDTO(
                        r.getUserId(), r.getParkingSpaceId(),
                        r.getStartTime().toString(), r.getEndTime().toString(), r.getStatus()
                )).toList();
    }

    @Override
    public int updateReservationStatusToPaid(Long id) {
        Optional<Reservation> optional = reservationRepo.findById(id);
        if (optional.isPresent()) {
            Reservation reservation = optional.get();
            reservation.setStatus("PAID");
            reservationRepo.save(reservation);
            return VarList.OK;
        }
        return VarList.Not_Acceptable;
    }

    @Override
    public List<ReservationDTO> getActiveReservationsByUser(Long userId) {
        return reservationRepo.findByUserIdAndStatus(userId, "ACTIVE")
                .stream()
                .map(this::mapToDTO)
                .toList();
    }
    private ReservationDTO mapToDTO(Reservation r) {
        return new ReservationDTO(
                r.getUserId(),
                r.getParkingSpaceId(),
                r.getStartTime().toString(),
                r.getEndTime().toString(),
                r.getStatus()
        );
    }



}

