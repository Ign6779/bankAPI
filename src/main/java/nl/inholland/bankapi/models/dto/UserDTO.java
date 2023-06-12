package nl.inholland.bankapi.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Role;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private List<Role> roles;
    private String phone;
    private double dayLimit;
    private double transactionLimit;
    private List<BankAccount> bankAccounts;
}
