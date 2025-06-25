package com.example.paymentservice.service;

import com.example.paymentservice.dto.TransactionDTO;
import com.example.paymentservice.entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionService {
    Transaction makePayment(TransactionDTO dto);
    Optional<Transaction> getTransactionById(Long id);

}
