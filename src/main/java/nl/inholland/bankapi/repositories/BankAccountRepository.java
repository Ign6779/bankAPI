package nl.inholland.bankapi.repositories;

import nl.inholland.bankapi.models.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
//    Optional<BankAccount> findBankAccountByRealIban(String iban);
}
