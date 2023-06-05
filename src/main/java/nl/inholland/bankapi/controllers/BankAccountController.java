package nl.inholland.bankapi.controllers;

import lombok.Data;
import lombok.extern.java.Log;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.dto.*;
import nl.inholland.bankapi.services.BankAccountService;
import nl.inholland.bankapi.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("bankAccounts")
@Log
public class BankAccountController {
    private final BankAccountService bankAccountService;
    private final UserService userService;

    public BankAccountController(BankAccountService bankAccountService, UserService userService) {
        this.bankAccountService = bankAccountService;
        this.userService = userService;
    }

    // we will need Get Methods -Beth

    @GetMapping("/{iban}")
    public ResponseEntity getBankAccount(@PathVariable String iban) {
        try {
            return ResponseEntity.status(200).body(bankAccountService.getBankAccountById(iban));
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity getAllBankAccounts(@RequestParam(required = false, defaultValue = "0") Integer page,
                                             @RequestParam(required = false, defaultValue = "100") Integer size) {
        try {
            return ResponseEntity.status(200).body(bankAccountService.getAllBankAccounts(page, size));
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @PostMapping("/search")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'CUSTOMER')")
    public ResponseEntity getIbanByUserFullName(@RequestBody SearchDTO searchDTO) {
        try {
            return ResponseEntity.status(200).body(bankAccountService.getBankAccountByUserFullName(searchDTO).stream().map(bankAccount -> mapBankAccountToSearchBankAccountDTO(bankAccount)));
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @PostMapping // create/add
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity addBankAccount(@RequestBody BankAccountDTO dto) throws Exception {
        try {
            return ResponseEntity.status(200).body(bankAccountService.createBankAccount(dto));
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @PutMapping("/{iban}")// edit/update
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity updateBankAccount(@PathVariable String iban, @RequestBody BankAccount bankAccount) {
        try {
            return ResponseEntity.status(200).body(bankAccountService.updateBankAccount(iban, bankAccount));
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    private ResponseEntity handleException(Exception e) {
        ExceptionDTO dto = new ExceptionDTO(e.getClass().getName(), e.getMessage());
        return ResponseEntity.status(400).body(dto);
    }

    /* I didn't put delete method because we cannot delete a bank account,
     we can only deactivate it which is also updating.
     but for that we will ned an isActive property for a bankAccount */

    private SearchBankAccountDTO mapBankAccountToSearchBankAccountDTO(BankAccount bankAccount) {
        SearchBankAccountDTO searchBankAccountDTO = new SearchBankAccountDTO();
        searchBankAccountDTO.setIban(bankAccount.getIban());
        return searchBankAccountDTO;
    }



    /*    @GetMapping
        public ResponseEntity getBankAccountByUserName(@RequestParam String userName) {
            try {
                return ResponseEntity.status(200).body(bankAccountService.getBankAccountByUserName(userName));
            } catch (Exception e) {
               return this.handleException(e);
            }
        }

        @GetMapping // get bank account by user id
        public ResponseEntity getBankAccountByUserId(@RequestParam UUID userId) {
            try {
                return ResponseEntity.status(200).body(bankAccountService.getBankAccountByUserId(userId));
            } catch (Exception e) {
                return this.handleException(e);
            }
        }*/

}
