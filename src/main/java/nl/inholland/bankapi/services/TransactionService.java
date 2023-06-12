package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.AccountType;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.dto.TransactionDTO;
import nl.inholland.bankapi.repositories.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private BankAccountService bankAccountService;

    public TransactionService(TransactionRepository transactionRepository, BankAccountService bankAccountService) {
        this.transactionRepository = transactionRepository;
        this.bankAccountService = bankAccountService;
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
                        && (amount == null || transaction.getAmount() == (amount))
                        && (highestAmount == null || transaction.getAmount() < highestAmount)
                        && (lowestAmount == null || transaction.getAmount() > lowestAmount))
                .toList();
        return transactions.stream()
                .toList();
    }

    public Transaction addTransaction(Transaction transaction) {
        transaction.setAccountTo(bankAccountService.getBankAccountById(transaction.getAccountTo().getIban()));
        transaction.setAccountFrom(bankAccountService.getBankAccountById(transaction.getAccountFrom().getIban()));
        List<Transaction> transactionsOfSender = transactionRepository.findByAccountFrom(transaction.getAccountFrom(), PageRequest.of(0, 100)).getContent();
        transaction.setTimeStamp(java.time.LocalDateTime.now());
        double amountPerDay = 0;
        for (Transaction transaction1 : transactionsOfSender) {
            if (transaction1.getAccountFrom().getIban().equals(transaction.getAccountFrom().getIban()) && transaction1.getTimeStamp().getDayOfMonth() == transaction.getTimeStamp().getDayOfMonth()) {
                amountPerDay += transaction1.getAmount();
            }
        }
        double totalAmount = amountPerDay + transaction.getAmount();
        if (!transaction.getAccountFrom().getType().equals(AccountType.BANK)) {
            if (transaction.getAccountFrom().getBalance() < transaction.getAmount()) {
                throw new IllegalArgumentException("Insufficient funds");
            } else if (transaction.getAccountFrom().getBalance() < 0) {
                throw new IllegalArgumentException("Balance cannot be negative");
            } else if (transaction.getAccountFrom().equals(transaction.getAccountTo())) {
                throw new IllegalArgumentException("Cannot transfer to the same account");
            } else if (transaction.getAccountFrom().getAbsoluteLimit() > transaction.getAccountFrom().getBalance() - transaction.getAmount()) {
                throw new IllegalArgumentException("Account balance falls below absolute limit");
            } else if (transaction.getAccountFrom().getUser().getTransactionLimit() < transaction.getAmount()) {
                throw new IllegalArgumentException("Amount exceeds transaction limit");
            } else if (transaction.getAccountFrom().getUser().getDayLimit() < totalAmount) {
                throw new IllegalArgumentException("Amount exceeds day limit");
            } else if (transaction.getAccountFrom().getType() == AccountType.SAVINGS && transaction.getAccountTo().getType() == AccountType.CURRENT
                    && !transaction.getAccountFrom().getUser().getId().equals(transaction.getAccountTo().getUser().getId())) {
                throw new IllegalArgumentException("Cannot transfer from savings to current account of another user");
            } else if (transaction.getAccountFrom().getType() == AccountType.CURRENT && transaction.getAccountTo().getType() == AccountType.SAVINGS
                    && !transaction.getAccountFrom().getUser().getId().equals(transaction.getAccountTo().getUser().getId())) {
                throw new IllegalArgumentException("Cannot transfer from current to savings account of another user");
            }
        }
        transaction.getAccountFrom().setBalance(transaction.getAccountFrom().getBalance() - transaction.getAmount());
        transaction.getAccountTo().setBalance(transaction.getAccountTo().getBalance() + transaction.getAmount());
        bankAccountService.updateBankAccount(transaction.getAccountFrom().getIban(), transaction.getAccountFrom(), true);
        bankAccountService.updateBankAccount(transaction.getAccountTo().getIban(), transaction.getAccountTo(), true);

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

    private TransactionDTO mapDtoToTransaction(Transaction transaction) {
        TransactionDTO dto = new TransactionDTO();
        dto.setAccountFrom(transaction.getAccountFrom());
        dto.setAccountTo(transaction.getAccountTo());
        dto.setAmount(transaction.getAmount());
        dto.setTimeStamp(transaction.getTimeStamp());

        return dto;
    }

    public Double getsSumOfAmountsByAccountAndDate(BankAccount bankAccount) {
        LocalDate currentTime = LocalDate.now();
        return transactionRepository.getSumOfAmountsByAccountAndDate(bankAccount.getIban(), currentTime.atStartOfDay());
    }
}