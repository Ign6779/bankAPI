package nl.inholland.bankapi.services;

import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.repositories.TransactionRepository;

public class TransactionService {
    private TransactionRepository transactionRepository;
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction addTransaction(Transaction transaction) {
        return this.transactionRepository.addTransaction(transaction);
    }

    public void updateTransaction(Transaction transaction) {
        this.transactionRepository.updateTransaction(transaction);
    }

    public void deleteTransaction(Transaction transaction) {
        this.transactionRepository.deleteTransaction(transaction);
    }
}
