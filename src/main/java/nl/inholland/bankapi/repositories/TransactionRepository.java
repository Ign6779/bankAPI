package nl.inholland.bankapi.repositories;

import nl.inholland.bankapi.models.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TransactionRepository {
    private List<Transaction> transactions;

    public TransactionRepository(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Transaction addTransaction(Transaction transaction) {
        transactions.add(transaction);
        return (transaction);
    }

    public void updateTransaction(Transaction transaction) {
        transactions.stream()
                .filter(t -> t.equals(transaction))
                .findFirst()
                .ifPresentOrElse(
                        t -> transactions.set(transactions.indexOf(t), transaction),
                        () -> {
                            throw new IllegalArgumentException("Transaction not found");
                        }
                );
    }

    public void deleteTransaction(Transaction transaction) {
        transactions.stream()
                .filter(t -> t.equals(transaction))
                .findFirst()
                .ifPresentOrElse(
                        t -> transactions.remove(t),
                        () -> {
                            throw new IllegalArgumentException("Transaction not found");
                        }
                );
    }
}
