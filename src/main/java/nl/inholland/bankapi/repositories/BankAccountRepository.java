package nl.inholland.bankapi.repositories;

import nl.inholland.bankapi.models.AccountType;
import nl.inholland.bankapi.models.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    Optional<BankAccount> findBankAccountByUserFirstNameIgnoreCaseAndUserLastNameIgnoreCaseAndType(String firstName, String lastName, AccountType accountType);

    Page<BankAccount> findAll(Pageable pageable);
}
