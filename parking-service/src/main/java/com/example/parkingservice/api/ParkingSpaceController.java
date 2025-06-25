package com.example.parkingservice.api;

import com.example.parkingservice.dto.ParkingSpaceDTO;
import com.example.parkingservice.dto.ResponseDTO;
import com.example.parkingservice.entity.ParkingSpace;
import com.example.parkingservice.service.ParkingSpaceService;
import com.example.parkingservice.utill.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
@RestController
@RequestMapping("/api/v1/parkings")
public class ParkingSpaceController {

    @Autowired
    private ParkingSpaceService service;

    @PostMapping("/add")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> add(@RequestBody ParkingSpaceDTO dto) {
        try {
            int res = service.addParkingSpace(dto);
            if (res == VarList.Created) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Added", dto));
            }
            return ResponseEntity.badRequest()
                    .body(new ResponseDTO(VarList.Bad_Gateway, "Failed", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> update(@PathVariable Long id,@RequestBody ParkingSpaceDTO dto) {
        try {
            int res = service.updateParkingSpace(id,dto);
            if (res == VarList.OK) {
                return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Updated", dto));
            }
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ResponseDTO(VarList.Not_Acceptable, "Not Found", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PatchMapping("/status/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            int res = service.changeStatus(id, status);
            if (res == VarList.OK) {
                return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Status Updated", null));
            }
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new ResponseDTO(VarList.Not_Acceptable, "Not Found", null));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/available")
    public ResponseEntity<ResponseDTO> getAvailable() {
        try {
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", service.getAvailableSpaces()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/zone/{zone}")
    public ResponseEntity<ResponseDTO> getByZone(@PathVariable String zone) {
        try {
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Success", service.getByZone(zone)));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PatchMapping("/reserve/{id}")
    public ResponseEntity<ResponseDTO> reserveSpot(@PathVariable Long id) {
        return updateStatus(id, "RESERVED");
    }

    @PatchMapping("/release/{id}")
    public ResponseEntity<ResponseDTO> releaseSpot(@PathVariable Long id) {
        return updateStatus(id, "AVAILABLE");
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<ResponseDTO> getById(@PathVariable Long id) {
        ParkingSpaceDTO dto = service.getById(id);
        if (dto != null) {
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Found", dto));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Acceptable, "Not Found", null));
        }
    }


}
