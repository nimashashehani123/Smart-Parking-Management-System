package com.example.reservationservice.api;

import com.example.reservationservice.dto.ReservationDTO;
import com.example.reservationservice.dto.ResponseDTO;
import com.example.reservationservice.entity.Reservation;
import com.example.reservationservice.service.ReservationService;
import com.example.reservationservice.utill.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> create(@RequestBody ReservationDTO dto) {
        try {
            int res = reservationService.createReservation(dto);
            if (res == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Reservation Created", ""));
            }
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Bad_Gateway, "Failed to reserve that Parking_Space", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PutMapping("/cancel/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> cancel(@PathVariable Long id) {
        try {
            int res = reservationService.cancelReservation(id);
            if (res == VarList.OK) {
                return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Cancelled", null));
            }
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ResponseDTO(VarList.Not_Acceptable, "Not Found", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDTO> getReservation(@PathVariable Long id) {
        ReservationDTO dto = reservationService.getReservation(id);
        if (dto != null) {
            System.out.println(new ResponseDTO(VarList.OK, "Found", dto));
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Found", dto));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ResponseDTO(VarList.Not_Acceptable, "Not Found", null));
        }
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> getByUser(@PathVariable Long userId) {
        try {
            List<ReservationDTO> list = reservationService.getReservationsByUser(userId);
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", list));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PatchMapping("/status/{id}")
    public ResponseEntity<ResponseDTO>updateStatus(@PathVariable Long id) {
        try {
            int result = reservationService.updateReservationStatusToPaid(id);
            if (result == VarList.OK) {
                return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Status Updated to PAID", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Acceptable, "Reservation Not Found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }


    @GetMapping("/user/active/{userId}")
    public List<ReservationDTO> getActive(@PathVariable Long userId) {
        return reservationService.getActiveReservationsByUser(userId);

    }


}
