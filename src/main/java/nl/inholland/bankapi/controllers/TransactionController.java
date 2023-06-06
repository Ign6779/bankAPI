package nl.inholland.bankapi.controllers;

import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.dto.ExceptionDTO;
import nl.inholland.bankapi.models.dto.TransactionDTO;
import nl.inholland.bankapi.services.BankAccountService;
import nl.inholland.bankapi.services.TransactionService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("transactions")

public class TransactionController {
    private TransactionService transactionService;
    private BankAccountService bankAccountService;

    public TransactionController(TransactionService transactionService, BankAccountService bankAccountService){
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    public ResponseEntity getAllTransactions(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "100") Integer size,
            @RequestParam(required = false) String accountFrom,
            @RequestParam(required = false) String accountTo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy/M/d") LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy/M/d") LocalDate dateTo) {
        try {

            BankAccount bankAccountFrom = null;
            BankAccount bankAccountTo = null;

            if (accountFrom != null) {
                bankAccountFrom = bankAccountService.getBankAccountById(accountFrom);
            }

            if (accountTo != null) {
                bankAccountTo = bankAccountService.getBankAccountById(accountTo);
            }

            return ResponseEntity.ok(transactionService.getAllTransactions(page, size, bankAccountFrom, bankAccountTo, dateFrom.atStartOfDay(), dateTo.atStartOfDay().plusDays(1).minusNanos(1)));
        } catch (Exception e) {
            return this.handleException(e);
        }
    }


    @PostMapping
    public ResponseEntity createTransaction(@RequestBody Transaction transaction) {
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

    private boolean isTransactionFieldsValid(Transaction transaction) {
        return transaction.getAmount() > 0 && transaction.getAccountTo() != null
                && transaction.getAccountFrom() != null;
    }
}