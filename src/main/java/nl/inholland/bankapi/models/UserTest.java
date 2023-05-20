package nl.inholland.bankapi.models;

import jakarta.persistence.*;
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
    private UUID id;
    private String email;

    private String password;
    private String firstName;

    private String lastName;
    private String phone;
    private double dayLimit;
    private double transactionLimit;
    @ElementCollection(fetch = FetchType.EAGER)
    private List <Role> roles;

    @OneToMany(mappedBy = "userTest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BankAccount> bankAccounts;

    public UserTest(String email,String password , String firstName,String lastName, String phone, double dayLimit, double transactionLimit, List<Role> roles) {
        this.email = email;
        this.password=password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.dayLimit = dayLimit;
        this.transactionLimit = transactionLimit;
        this.roles = roles;
    }
}
