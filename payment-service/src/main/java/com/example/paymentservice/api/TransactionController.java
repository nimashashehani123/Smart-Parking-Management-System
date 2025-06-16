package com.example.paymentservice.api;

import com.example.paymentservice.dto.TransactionDTO;
import com.example.paymentservice.entity.Transaction;
import com.example.paymentservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/payments")
public class TransactionController {

    @Autowired
    private TransactionService service;

    @PostMapping
    public Transaction makePayment(@RequestBody TransactionDTO dto) {
        return service.makePayment(dto);
    }

    @GetMapping("/user/{userId}")
    public List<Transaction> getUserTransactions(@PathVariable Long userId) {
        return service.getUserTransactions(userId);
    }
}
