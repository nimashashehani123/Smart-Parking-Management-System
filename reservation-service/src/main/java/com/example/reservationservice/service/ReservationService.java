package com.example.reservationservice.service;

import com.example.reservationservice.dto.ReservationDTO;
import com.example.reservationservice.entity.Reservation;

import java.util.List;

public interface ReservationService {
    Reservation createReservation(ReservationDTO dto);
    List<Reservation> getReservationsByUser(Long userId);
    List<Reservation> getAllReservations();
}
