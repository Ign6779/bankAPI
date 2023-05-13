package nl.inholland.bankapi.controllers;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.dto.ExceptionDTO;
import nl.inholland.bankapi.models.dto.TransactionDTO;
import nl.inholland.bankapi.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("transactions")

public class TransactionController {
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @PostMapping
    public ResponseEntity addTransaction(@RequestBody TransactionDTO transaction) {
        try {
            return ResponseEntity.status(201).body(
                    transactionService.addTransaction(transaction)
            );
        } catch (Exception e) {
            // This exposes too much data
            return this.handleException(400, e);
        }
    }

    @GetMapping(value = "{id}")
    public ResponseEntity getTransactionById(@PathVariable UUID id) {
        try {
            return ResponseEntity.ok(transactionService.getTransactionById(id));
        } catch (EntityNotFoundException enfe) {
            return this.handleException(404, enfe);
        }
    }

    //TODO: Returns no body. Fix that
    @PutMapping("{id}")
    public ResponseEntity updateTransaction(@PathVariable UUID id, @RequestBody TransactionDTO dto) {
        try {
            Transaction toUpdate = transactionService.updateTransaction(id, dto);
            return ResponseEntity.status(204).body(toUpdate);
        } catch (Exception e) {
            return this.handleException(400, e);
        }
    }

    private ResponseEntity handleException(int status, Exception e) {
        ExceptionDTO dto = new ExceptionDTO(e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(status).body(dto);
    }
}
