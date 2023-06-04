package nl.inholland.bankapi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.inholland.bankapi.models.AccountType;
import nl.inholland.bankapi.models.User;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BankAccountDTO {
    private UserDTO user;
    private double absoluteLimit;
    private double balance;
    private AccountType type;

}
