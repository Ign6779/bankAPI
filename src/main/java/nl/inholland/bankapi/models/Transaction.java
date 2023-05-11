package nl.inholland.bankapi.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue
    private UUID uuid;

    private LocalDate timeStamp;
    private Long accountFrom; //iban
    private Long accountTo; //iban
    private double amount;
    private String performingUser;
}
