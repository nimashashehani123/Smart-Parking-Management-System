package com.example.paymentservice.service;

import com.example.paymentservice.dto.TransactionDTO;
import com.example.paymentservice.entity.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction makePayment(TransactionDTO dto);
    List<Transaction> getUserTransactions(Long userId);
}
