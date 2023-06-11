package nl.inholland.bankapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    private UUID uuid;

    private LocalDateTime timeStamp;
    @ManyToOne
    private BankAccount accountFrom;
    @ManyToOne
    private BankAccount accountTo;
    private double amount;
    private String performingUser;

    public Transaction(LocalDateTime timeStamp, BankAccount accountFrom, BankAccount accountTo, double amount, String performingUser) {
        this.timeStamp = timeStamp;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
        this.performingUser = performingUser;
    }
}