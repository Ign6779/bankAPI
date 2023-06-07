package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.AccountType;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.dto.TransactionDTO;
import nl.inholland.bankapi.models.dto.UserDTO;
import nl.inholland.bankapi.repositories.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }
  
    public List<TransactionDTO> getAllTransactions(Integer page, Integer size, BankAccount accountFrom, BankAccount accountTo, LocalDateTime dateFrom, LocalDateTime dateTo, Double amount, Double highestAmount, Double lowestAmount) {
        PageRequest pageable = PageRequest.of(page, size);
        List<TransactionDTO> transactions;
        if (accountFrom != null) {
            transactions = transactionRepository.findByAccountFrom(accountFrom, pageable).getContent().stream().map(transaction -> mapDtoToTransaction(transaction)).toList();
        } else if (accountTo != null) {
            transactions = transactionRepository.findByAccountTo(accountTo, pageable).getContent().stream().map(transaction -> mapDtoToTransaction(transaction)).toList();
        } else {
            transactions = transactionRepository.findAll(pageable).getContent().stream().map(transaction -> mapDtoToTransaction(transaction)).toList();
        }
        transactions = transactions.stream()
                .filter(transaction -> (dateFrom == null || transaction.getTimeStamp().isAfter(dateFrom))
                        && (dateTo == null || transaction.getTimeStamp().isBefore(dateTo))
                        && (amount == null || transaction.getAmount()==(amount))
                        && (highestAmount == null || transaction.getAmount() < highestAmount)
                        && (lowestAmount == null || transaction.getAmount() > lowestAmount))
                .toList();
        return transactions.stream()
                .toList();
    }

    public Transaction addTransaction(Transaction transaction) {
        if(transaction.getAccountFrom().getBalance() < transaction.getAmount()){
            throw new IllegalArgumentException("Insufficient funds");
        } else if (transaction.getAccountFrom().getBalance() < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        } else if (transaction.getAccountFrom().equals(transaction.getAccountTo())) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        } else if (transaction.getAccountFrom().getAbsoluteLimit() > transaction.getAccountFrom().getBalance() - transaction.getAmount()) {
            throw new IllegalArgumentException("Account balance falls below absolute limit");
        } else if (transaction.getAccountFrom().getUser().getTransactionLimit()<transaction.getAmount()) {
            throw new IllegalArgumentException("Amount exceeds transaction limit");
        } else if (transaction.getAccountFrom().getUser().getDayLimit()<transaction.getAmount()) {
            throw new IllegalArgumentException("Amount exceeds day limit");
        } else if (transaction.getAccountFrom().getType()== AccountType.SAVINGS && transaction.getAccountTo().getType()== AccountType.CURRENT
        && transaction.getAccountFrom().getUser()!=transaction.getAccountTo().getUser()) {
            throw new IllegalArgumentException("Cannot transfer from savings to current account of another user");
        } else if (transaction.getAccountFrom().getType()== AccountType.CURRENT && transaction.getAccountTo().getType()== AccountType.SAVINGS
            && transaction.getAccountFrom().getUser()!=transaction.getAccountTo().getUser()) {
            throw new IllegalArgumentException("Cannot transfer from current to savings account of another user");
        } else {
            transaction.getAccountFrom().setBalance(transaction.getAccountFrom().getBalance() - transaction.getAmount());
            transaction.getAccountTo().setBalance(transaction.getAccountTo().getBalance() + transaction.getAmount());
        }
        return transactionRepository.save(transaction);
    }

    public Transaction getTransactionById(UUID id) {
        return transactionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Transaction not found")
        );
    }

    public Transaction updateTransaction(UUID id, TransactionDTO dto) {
        Transaction transactionToUpdate = transactionRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        transactionToUpdate.setAccountFrom(dto.getAccountFrom());
        transactionToUpdate.setAccountTo(dto.getAccountTo());
        transactionToUpdate.setAmount(dto.getAmount());

        return transactionRepository.save(transactionToUpdate);
    }

    private TransactionDTO mapDtoToTransaction(Transaction transaction){
        TransactionDTO dto = new TransactionDTO();
        dto.setAccountFrom(transaction.getAccountFrom());
        dto.setAccountTo(transaction.getAccountTo());
        dto.setAmount(transaction.getAmount());
        dto.setTimeStamp(transaction.getTimeStamp());

        return dto;
    }


}