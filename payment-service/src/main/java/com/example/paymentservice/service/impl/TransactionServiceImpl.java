package com.example.paymentservice.service.impl;

import com.example.paymentservice.dto.TransactionDTO;
import com.example.paymentservice.entity.Transaction;
import com.example.paymentservice.repository.TransactionRepository;
import com.example.paymentservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionRepository repository;

    @Override
    public Transaction makePayment(TransactionDTO dto) {
        Transaction t = new Transaction();
        t.setUserId(dto.getUserId());
        t.setReservationId(dto.getReservationId());
        t.setAmount(dto.getAmount());
        t.setStatus(dto.getStatus());
        return repository.save(t);
    }

    @Override
    public List<Transaction> getUserTransactions(Long userId) {
        return repository.findByUserId(userId);
    }
}
