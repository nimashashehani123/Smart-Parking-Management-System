package com.example.userservice.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @GetMapping("all")
    public String getAllProduct(){
        return "Prod1, Prod2 ,Prod3";
    }
}
