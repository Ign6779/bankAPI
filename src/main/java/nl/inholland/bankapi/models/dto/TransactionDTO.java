package nl.inholland.bankapi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.inholland.bankapi.models.BankAccount;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDTO {
    private BankAccount accountFrom;
    private BankAccount accountTo;
    private double amount;
    private LocalDateTime timeStamp;
}