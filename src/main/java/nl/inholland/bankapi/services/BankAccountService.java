package nl.inholland.bankapi.services;

import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.repositories.BankAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class BankAccountService {
    private BankAccountRepository bankAccountRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public BankAccount addBankAccount(BankAccount bankAccount) {
        return this.bankAccountRepository.addBankAccount(bankAccount);
    }

    public void updateBankAccount(BankAccount bankAccount) {
        this.bankAccountRepository.updateBankAccount(bankAccount);
    }

    public void deleteBankAccount(BankAccount bankAccount) {
        this.bankAccountRepository.deleteBankAccount(bankAccount);
    }
}
