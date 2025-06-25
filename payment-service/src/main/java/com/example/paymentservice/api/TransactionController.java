package com.example.paymentservice.api;

import com.example.paymentservice.dto.ResponseDTO;
import com.example.paymentservice.dto.TransactionDTO;
import com.example.paymentservice.entity.Transaction;
import com.example.paymentservice.service.TransactionService;
import com.example.paymentservice.utill.ReceiptGenerator;
import com.example.paymentservice.utill.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/payments")
public class TransactionController {

    @Autowired
    private TransactionService service;
    @PostMapping("/make")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ResponseDTO> pay(@RequestBody TransactionDTO dto) {
        try {
            Transaction tx = service.makePayment(dto);
            if ("SUCCESS".equals(tx.getStatus())) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Payment Successful", tx));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.Bad_Gateway, "Payment Failed", tx));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/receipt/{transactionId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<byte[]> getReceipt(@PathVariable Long transactionId) {
        Optional<Transaction> tx = service.getTransactionById(transactionId);

        if (tx.isPresent()) {
            byte[] pdf = ReceiptGenerator.generate(tx.get());
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=receipt_" + transactionId + ".pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdf);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }


}
