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

    @ManyToOne
    private UserTest userTest;

    public BankAccount(UserTest userTest, double absoluteLimit, double balance, AccountType type) {
        this.userTest = userTest;
        this.absoluteLimit = absoluteLimit;
        this.balance = balance;
        this.type = type;
    }

    private double absoluteLimit;
    private double balance;
    private AccountType type;

    public enum AccountType {
        CURRENT,
        SAVINGS
    }
}
