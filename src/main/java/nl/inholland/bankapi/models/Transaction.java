package nl.inholland.bankapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    private int id;
    private String uuid;
    private LocalDate timeStamp;
    private String accountFrom;
    private String accountTo;
    private double amount;
    private String performingUser;
}
