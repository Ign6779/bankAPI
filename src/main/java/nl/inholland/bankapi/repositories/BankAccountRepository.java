package nl.inholland.bankapi.repositories;

import nl.inholland.bankapi.models.BankAccount;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BankAccountRepository {

    private List<BankAccount> bankAccounts;

    public BankAccountRepository(List<BankAccount> bankAccounts) {
        this.bankAccounts = bankAccounts;
    }

    public BankAccount addBankAccount(BankAccount bankAccount) {
        bankAccounts.add(bankAccount);
        return (bankAccount);
    }

    public void updateBankAccount(BankAccount bankAccount) {
        bankAccounts.stream()
                .filter(b -> b.equals(bankAccount))
                .findFirst()
                .ifPresentOrElse(
                        b -> bankAccounts.set(bankAccounts.indexOf(b), bankAccount),
                        () -> {
                            throw new IllegalArgumentException("Bank account not found");
                        }
                );
    }

    public void deleteBankAccount(BankAccount bankAccount) {
        bankAccounts.stream()
                .filter(b -> b.equals(bankAccount))
                .findFirst()
                .ifPresentOrElse(
                        b -> bankAccounts.remove(b),
                        () -> {
                            throw new IllegalArgumentException("Bank account not found");
                        }
                );
    }
}
