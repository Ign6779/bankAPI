package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.function.Consumer;
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

    public BankAccount getBankAccountById(String iban) {
        return bankAccountRepository.findById(iban).orElseThrow(EntityNotFoundException::new);
    }

/*    public BankAccount getBankAccountByUserName(String userName) {
        return bankAccountRepository.findByUserName(userName).orElseThrow(EntityNotFoundException::new);
    }
    public BankAccount getBankAccountByUserId(long userId) {
        return bankAccountRepository.findByUserId(userId).orElseThrow(EntityNotFoundException::new);
    }*/
    public void addBankAccount(BankAccount bankAccount) {
        String iban;
        do{
            iban= generateIban();
            bankAccount.setIban(iban);
        }while (!bankAccountRepository.findById(iban).isEmpty());
        bankAccountRepository.save(bankAccount);
    }

    public BankAccount updateBankAccount(String iban, BankAccount bankAccount) {
        BankAccount bankAccountToUpdate = bankAccountRepository.findById(iban)
                        .orElseThrow(() -> new EntityNotFoundException("Bank account with id " + iban + " not found"));
        updateBankAccountField(bankAccount.getAbsoluteLimit(), bankAccountToUpdate::setAbsoluteLimit);
        updateBankAccountField(bankAccount.isAvailable(), bankAccountToUpdate::setAvailable);
        return bankAccountRepository.save(bankAccountToUpdate );
    }

    private <T> void updateBankAccountField(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    private String generateIban(){
        String countryCode = "NL";
        String bankCode = "INHO0";
        String accountNumber= generateRandomAccountNumber();
        return countryCode+bankCode+accountNumber;
    }

    private static String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();

        // Generate 9 random digits
        for (int i = 0; i < 9; i++) {
            int digit = random.nextInt(10);
            accountNumber.append(digit);
        }

        return accountNumber.toString();
    }
}
