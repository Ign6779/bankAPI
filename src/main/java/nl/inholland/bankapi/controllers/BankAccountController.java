package nl.inholland.bankapi.controllers;

import lombok.extern.java.Log;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.dto.BankAccountDTO;
import nl.inholland.bankapi.models.dto.ExceptionDTO;
import nl.inholland.bankapi.models.dto.SearchBankAccountDTO;
import nl.inholland.bankapi.models.dto.SearchDTO;
import nl.inholland.bankapi.services.BankAccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bankAccounts")
@Log
public class BankAccountController {
    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
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
            if (!isBankAccountFieldsValid(dto)) {
                throw new Exception("Required fields are missing.");
            }
            return ResponseEntity.status(201).body(bankAccountService.createBankAccount(dto));
        } catch (Exception e) {
            return this.handleException(e);
        }
    }

    @PutMapping("/{iban}")// edit/update
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity updateBankAccount(@PathVariable String iban, @RequestBody BankAccount bankAccount) {
        try {
            return ResponseEntity.status(200).body(bankAccountService.updateBankAccount(iban, bankAccount, false));
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

    private boolean isBankAccountFieldsValid(BankAccountDTO bankAccount) {
        return bankAccount.getType() != null && !bankAccount.getType().toString().isEmpty() &&
                (Double) bankAccount.getBalance() != null &&
                (Double) bankAccount.getAbsoluteLimit() != null &&
                !bankAccount.getUserId().toString().isEmpty() && bankAccount.getUserId() != null;
    }


}
