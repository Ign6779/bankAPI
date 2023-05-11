package nl.inholland.bankapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue
    private Long iban; //ive no idea how to create ibans for now. ive some code but its wrong

    private String userUuid;
    private double absoluteLimit;
    private double balance;
    private AccountType type;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public enum AccountType {
        CURRENT,
        SAVINGS
    }
}
