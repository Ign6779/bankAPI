package nl.inholland.bankapi.repositories;

import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, UUID> {

    Page<Transaction> findByAccountFrom(BankAccount accountFrom, Pageable pageable);
    Page<Transaction> findByAccountTo(BankAccount accountFrom, Pageable pageable);
    Page <Transaction> findAll(Pageable pageable);
}
