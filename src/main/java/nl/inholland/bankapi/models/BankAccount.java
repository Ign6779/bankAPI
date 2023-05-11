package nl.inholland.bankapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue
    private Long iban; //ive no idea how to create ibans for now. ive some code but its wrong

    private String userUuid;

    public BankAccount(String userUuid, double absoluteLimit, double balance, AccountType type) {
        this.userUuid = userUuid;
        this.absoluteLimit = absoluteLimit;
        this.balance = balance;
        this.type = type;
    }

    private double absoluteLimit;
    private double balance;
    private AccountType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserTest userTest;

    public enum AccountType {
        CURRENT,
        SAVINGS
    }
}
