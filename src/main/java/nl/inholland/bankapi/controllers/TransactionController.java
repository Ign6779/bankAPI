package nl.inholland.bankapi.controllers;

import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.dto.ExceptionDTO;
import nl.inholland.bankapi.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("transactions")

public class TransactionController {
//    private TransactionService transactionService;
//
//    public TransactionController (TransactionService transactionService){
//        this.transactionService = transactionService;
//    }
//
//    // we will need Get Methods -Beth
//
//    @PostMapping // create/add
//    public ResponseEntity addTransaction(@RequestBody Transaction transaction) {
//        return ResponseEntity.status(201).body(
//                Collections.singletonMap("id", transactionService.addTransaction(transaction))
//        );
//    }
//
//    // although I am not sure if we need update and delete methods for a transaction -Beth
//
//    @PutMapping // edit/update
//    public ResponseEntity updateTransaction(@RequestBody Transaction transaction) {
//        try {
//            transactionService.updateTransaction(transaction);
//            return ResponseEntity.status(204).body(null);
//        } catch (Exception e) {
//            return this.handleException(e);
//        }
//    }
//
//    @DeleteMapping // delete
//    public ResponseEntity deleteTransaction(@RequestBody Transaction transaction) {
//        try {
//            transactionService.deleteTransaction(transaction);
//            return ResponseEntity.status(204).body(null);
//        } catch (Exception e) {
//            return this.handleException(e);
//        }
//    }
//
//    private ResponseEntity handleException(Exception e) {
//        ExceptionDTO dto = new ExceptionDTO(e.getClass().getName(), e.getMessage());
//        return ResponseEntity.status(400).body(dto);
//    }
}
