package com.example.reservationservice.service;

import com.example.reservationservice.dto.ReservationDTO;
import com.example.reservationservice.entity.Reservation;

import java.util.List;

public interface ReservationService {
    int createReservation(ReservationDTO dto);
    int cancelReservation(Long id);
    ReservationDTO getReservation(Long id);
    List<ReservationDTO> getReservationsByUser(Long userId);
    int updateReservationStatusToPaid(Long id);
    List<ReservationDTO> getActiveReservationsByUser(Long userId);

}
