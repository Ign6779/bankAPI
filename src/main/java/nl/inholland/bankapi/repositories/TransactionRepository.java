package nl.inholland.bankapi.repositories;

import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, UUID> {

    Optional<Transaction> findTransactionByAccountFromIban(String accountFrom);
}
