package com.example.reservationservice.api;

import com.example.reservationservice.dto.ReservationDTO;
import com.example.reservationservice.entity.Reservation;
import com.example.reservationservice.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public Reservation create(@RequestBody ReservationDTO dto) {
        return reservationService.createReservation(dto);
    }

    @GetMapping("/user/{userId}")
    public List<Reservation> getByUser(@PathVariable Long userId) {
        return reservationService.getReservationsByUser(userId);
    }

    @GetMapping
    public List<Reservation> getAll() {
        return reservationService.getAllReservations();
    }
}
