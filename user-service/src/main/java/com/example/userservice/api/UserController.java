package com.example.userservice.api;
import com.example.userservice.dto.AuthDTO;
import com.example.userservice.dto.ReservationDTO;
import com.example.userservice.dto.ResponseDTO;
import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;
import com.example.userservice.service.impl.UserServiceImpl;
import com.example.userservice.utill.JwtUtil;
import com.example.userservice.utill.VarList;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    private final AuthenticationManager authenticationManager;

    public UserController(JwtUtil jwtUtil, AuthenticationManager authenticationManager, UserServiceImpl userService) {
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody UserDTO userDTO) {
        try {
            int res = userService.registerUser(userDTO);
            return switch (res) {
                case VarList.Created -> ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Success", userDTO));
                case VarList.Not_Acceptable -> ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                        .body(new ResponseDTO(VarList.Not_Acceptable, "Email Already Used", null));
                default -> ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                        .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            };
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> authenticate(@RequestBody UserDTO userDTO, HttpServletRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ResponseDTO(VarList.Unauthorized, "Invalid Credentials", e.getMessage()));
        }

        UserDTO loadedUser = userService.loadUserDetailsByUsername(userDTO.getEmail());
        if (loadedUser == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
        }

        String token = jwtUtil.generateToken(loadedUser);
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ResponseDTO(VarList.Conflict, "Authorization Failure! Please Try Again", null));
        }

        AuthDTO authDTO = new AuthDTO();
        authDTO.setEmail(loadedUser.getEmail());
        authDTO.setToken(token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDTO(VarList.Created, "Success", authDTO));
    }

    @PutMapping("/updateUser/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO dto) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
        }

        User updated = userService.updateUser(id, dto);
        return ResponseEntity.ok(new ResponseDTO(VarList.OK, "User updated successfully", updated));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ResponseDTO> getAllUsers(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        System.out.println("User-Service received Authorization header: " + authHeader);

        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(new ResponseDTO(VarList.OK, "All Users", users));
    }

    @GetMapping("/getUser/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Found, "User not found", null));
        }

        return ResponseEntity.ok(new ResponseDTO(VarList.OK, "User found", user));
    }

    @GetMapping("/bookings/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> getBookingHistory(@PathVariable Long userId) {
        try {
            List<ReservationDTO> history = userService.getUserReservationHistory(userId);
            if (history == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new ResponseDTO(VarList.Internal_Server_Error, "Failed to fetch booking history", null));
            }

            if (history.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                        .body(new ResponseDTO(VarList.No_Content, "No bookings found for this user", history));
            }
            return ResponseEntity.ok(new ResponseDTO(VarList.OK, "Booking history fetched successfully", history));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}

