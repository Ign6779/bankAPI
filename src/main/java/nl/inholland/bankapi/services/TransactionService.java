package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.dto.TransactionDTO;
import nl.inholland.bankapi.models.dto.UserDTO;
import nl.inholland.bankapi.repositories.TransactionRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }
  
    public List<TransactionDTO> getAllTransactions(Integer page, Integer size, BankAccount accountFrom, BankAccount accountTo) {
        PageRequest pageable = PageRequest.of(page, size);
        if (accountFrom != null) {
            return transactionRepository.findByAccountFrom(accountFrom, pageable)
                    .getContent()
                    .stream()
                    .map(transaction -> mapDtoToTransaction(transaction))
                    .toList();
        }
        if (accountTo != null) {
            return transactionRepository.findByAccountTo(accountTo, pageable)
                    .getContent()
                    .stream()
                    .map(transaction -> mapDtoToTransaction(transaction))
                    .toList();
        }
        return transactionRepository.findAll(pageable)
                .getContent()
                .stream()
                .map(transaction -> mapDtoToTransaction(transaction))
                .toList();
    }

    public Transaction addTransaction(Transaction transaction) {
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