package nl.inholland.bankapi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class TransactionDTO {
    private LocalDate timeStamp;
    private Long accountFrom; //iban
    private Long accountTo; //iban
    private double amount;
    private String performingUser;
}
