package nl.inholland.bankapi.controllers;

import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.services.BankAccountService;
import nl.inholland.bankapi.models.dto.ExceptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("bankAccounts")
public class BankAccountController {
    private BankAccountService bankAccountService;

    public BankAccountController (BankAccountService bankAccountService){
        this.bankAccountService = bankAccountService;
    }

    // we will need Get Methods -Beth

    @GetMapping
    public ResponseEntity getBankAccount(@RequestParam long iban) {
        try {
            return ResponseEntity.status(200).body(bankAccountService.getBankAccountById(iban));
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @GetMapping // get all
    public ResponseEntity getAllBankAccounts(@RequestParam(required = false) Integer offset,
                                             @RequestParam(required = false) Integer limit) {
        try {
            return ResponseEntity.status(200).body(bankAccountService.getAllBankAccounts(offset, limit));
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

//    @GetMapping
//    public ResponseEntity getBankAccountByUserName(@RequestParam String userName) {
//        try {
//            return ResponseEntity.status(200).body(bankAccountService.getBankAccountByUserName(userName));
//        } catch (Exception e) {
//            return this.handleException(e);
//        }
//    }
//
//    @GetMapping // get bank account by user id
//    public ResponseEntity getBankAccountByUserId(@RequestParam UUID userId) {
//        try {
//            return ResponseEntity.status(200).body(bankAccountService.getBankAccountByUserId(userId));
//        } catch (Exception e) {
//            return this.handleException(e);
//        }
//    }
//
    @PostMapping // create/add
    public ResponseEntity addBankAccount(@RequestBody BankAccount bankAccount) {
        try {
            bankAccountService.addBankAccount(bankAccount);
            return ResponseEntity.status(201).body(null);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @PutMapping // edit/update
    public ResponseEntity updateBankAccount(@RequestBody BankAccount bankAccount) {
        try {
            bankAccountService.updateBankAccount(bankAccount);
            return ResponseEntity.status(204).body(null);
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    /* I didn't put delete method because we cannot delete a bank account,
     we can only deactivate it which is also updating.
     but for that we will ned an isActive property for a bankAccount */

    private ResponseEntity handleException(Exception e) {
        ExceptionDTO dto = new ExceptionDTO(e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(400).body(dto);
    }
}
