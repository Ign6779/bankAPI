package nl.inholland.bankapi.controllers;

import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.services.BankAccountService;
import nl.inholland.bankapi.models.dto.ExceptionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("bankAccounts")
public class BankAccountController {
    private BankAccountService bankAccountService;

    public BankAccountController (BankAccountService bankAccountService){
        this.bankAccountService = bankAccountService;
    }

    // we will need Get Methods -Beth

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
