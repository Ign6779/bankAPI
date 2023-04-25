package nl.inholland.bankapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {
    private int id;
    private String iban;
    private String userUuid;
    private double absoluteLimit;
    private double balance;
    private AccountType type;

    public enum AccountType {
        CURRENT,
        SAVINGS
    }
}
