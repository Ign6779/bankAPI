package nl.inholland.bankapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int id;
    private String uuid;
    private String email;
    private String name;
    private String phone;
    private double dayLimit;
    private double transactionLimit;
    private Role role;

    enum Role {
        CUSTOMER,
        EMPLOYEE
    }
}
