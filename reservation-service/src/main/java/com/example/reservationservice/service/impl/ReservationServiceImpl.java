package com.example.reservationservice.service.impl;

import com.example.reservationservice.dto.ReservationDTO;
import com.example.reservationservice.entity.Reservation;
import com.example.reservationservice.repository.ReservationRepository;
import com.example.reservationservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    @Autowired
    private ReservationRepository repository;

    @Override
    public Reservation createReservation(ReservationDTO dto) {
        Reservation r = new Reservation();
        r.setUserId(dto.getUserId());
        r.setVehicleId(dto.getVehicleId());
        r.setParkingId(dto.getParkingId());
        r.setStartTime(dto.getStartTime());
        r.setEndTime(dto.getEndTime());
        r.setStatus(dto.getStatus());
        return repository.save(r);
    }

    @Override
    public List<Reservation> getReservationsByUser(Long userId) {
        return repository.findByUserId(userId);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return repository.findAll();
    }
}
