package nl.inholland.bankapi.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class BankAccount {

    @Id
    private String iban; //ive no idea how to create ibans for now. ive some code but its wrong

    @ManyToOne
    @JsonBackReference
    private User user;
    private double absoluteLimit;
    private double balance;
    private AccountType type;
    private boolean available;
    public BankAccount(User user, double absoluteLimit, double balance, AccountType type) {
        this.user = user;
        this.absoluteLimit = absoluteLimit;
        this.balance = balance;
        this.type = type;
        this.available = true;
    }


}
