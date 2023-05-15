package nl.inholland.bankapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class UserTest {

    @Id
    @GeneratedValue
    private long id;
    private String email;
    private String name;
    private String phone;
    private double dayLimit;
    private double transactionLimit;
    private Role role;

    @OneToMany(mappedBy = "userTest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BankAccount> bankAccounts;

    public UserTest( String email, String name, String phone, double dayLimit, double transactionLimit, Role role) {
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
        this.role = role;
    }

    public enum Role {
        CUSTOMER,
        EMPLOYEE
    }
}
