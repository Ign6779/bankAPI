package nl.inholland.bankapi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.inholland.bankapi.models.AccountType;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankAccountDTO {
    private UUID userId;
    private double absoluteLimit;
    private double balance;
    private AccountType type;

}
