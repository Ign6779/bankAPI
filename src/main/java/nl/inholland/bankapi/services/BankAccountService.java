package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository=bankAccountRepository;
    }

    public List<BankAccount> getAllBankAccounts(Integer offset, Integer limit) {
        List<BankAccount> allBankAccounts = (List<BankAccount>) bankAccountRepository.findAll();

        // Apply filtering based on the provided parameters
        if (offset != null && offset > 0) {
            allBankAccounts = allBankAccounts.stream()
                    .skip(offset)
                    .collect(Collectors.toList());
        }

        if (limit != null && limit > 0) {
            allBankAccounts = allBankAccounts.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        return allBankAccounts;
    }

    public BankAccount getBankAccountById(Long iban) {
        return bankAccountRepository.findById(iban).orElseThrow(EntityNotFoundException::new);
    }

    public void addBankAccount(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }

    public void updateBankAccount(BankAccount bankAccount) {
        bankAccountRepository.save(bankAccount);
    }

    public void deleteBankAccount(Long iban) {
        bankAccountRepository.deleteById(iban);
    }
}
