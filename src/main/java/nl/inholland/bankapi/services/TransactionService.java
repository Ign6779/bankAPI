package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.dto.TransactionDTO;
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

    public List<Transaction> getAllTransactions(Integer page, Integer size){
        PageRequest pageable = PageRequest.of(page, size);
        return transactionRepository.findAll(pageable).getContent();
    }

    public Transaction addTransaction(TransactionDTO dto) {
        return transactionRepository.save(this.mapDtoToTransaction(dto));
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

    private Transaction mapDtoToTransaction(TransactionDTO dto) {
        Transaction newTransaction = new Transaction();

        newTransaction.setAccountFrom(dto.getAccountFrom());
        newTransaction.setAccountTo(dto.getAccountTo());
        newTransaction.setAmount(dto.getAmount());

        return newTransaction;
    }
}