package nl.inholland.bankapi.controllers;

import nl.inholland.bankapi.models.dto.ExceptionDTO;
import nl.inholland.bankapi.models.dto.TransactionDTO;
import nl.inholland.bankapi.services.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("transactions")

public class TransactionController {
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService){
        this.transactionService = transactionService;
    }

    /*@GetMapping
    public ResponseEntity getAllTransactions(){
        try {
            return ResponseEntity.ok(transactionService.getAllTransactions());
        }
        catch (Exception e){
            return  this.handleException(e);
        }
    }*/

    @GetMapping
    public ResponseEntity getAllTransactions(@RequestParam(required = false,defaultValue = "0") Integer page,
                                             @RequestParam(required = false,defaultValue = "100") Integer size){
        try {
            return ResponseEntity.ok(transactionService.getAllTransactions(page, size));
        }
        catch (Exception e){
            return  this.handleException(e);
        }
    }

    @PostMapping
    public ResponseEntity createTransaction(@RequestBody TransactionDTO transaction) {
        try {
            if(isTransactionFieldsValid(transaction)){
                transactionService.addTransaction(transaction);
                return ResponseEntity.status(201).body(null);
            }else
            {
                return ResponseEntity.status(400).body("Invalid amount or field.");
            }

        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity getTransactionById(@PathVariable UUID id){
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity updateTransaction(@PathVariable UUID id,@RequestBody TransactionDTO transactionDTO) {
        try {
            transactionService.updateTransaction(id,transactionDTO);
            return ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    private ResponseEntity handleException(Exception e) {
        ExceptionDTO dto = new ExceptionDTO(e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(400).body(dto);
    }

    private boolean isTransactionFieldsValid(TransactionDTO transaction) {
        return transaction.getAmount() > 0 && transaction.getAccountTo() != null
                && transaction.getAccountFrom() != null;
    }
}