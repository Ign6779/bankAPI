package nl.inholland.bankapi.models;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_CUSTOMER,
    ROLE_EMPLOYEE;

    @Override
    public String getAuthority() {
        return name();
    }
}
