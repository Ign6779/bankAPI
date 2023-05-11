package nl.inholland.bankapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID uuid;

    private String email;
    private String name;
    private String phone;
    private double dayLimit;
    private double transactionLimit;
    private Role role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<BankAccount> bankAccounts;

    public enum Role {
        CUSTOMER,
        EMPLOYEE
    }
}
