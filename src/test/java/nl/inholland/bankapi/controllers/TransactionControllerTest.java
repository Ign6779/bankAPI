package nl.inholland.bankapi.controllers;

import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.dto.ExceptionDTO;
import nl.inholland.bankapi.models.dto.TransactionDTO;
import nl.inholland.bankapi.services.BankAccountService;
import nl.inholland.bankapi.services.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

public class TransactionControllerTest {
    private MockMvc mockMvc;

    @Mock
    private TransactionService transactionService;

    @Mock
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new TransactionController(transactionService, bankAccountService)).build();
    }

    @Test
    void testGetAllTransactions() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/transactions")
                        .param("page", "0")
                        .param("size", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(transactionService).getAllTransactions(eq(0), eq(100), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull());
    }

    @Test
    void testCreateTransaction_WithValidTransaction() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setAmount(100);
        transaction.setAccountFrom(new BankAccount());
        transaction.setAccountTo(new BankAccount());

        when(transactionService.addTransaction(any(Transaction.class))).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Replace with the JSON representation of your valid transaction
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(transactionService).addTransaction(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_WithInvalidTransaction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Replace with the JSON representation of your invalid transaction
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("Invalid amount or field."));

        verify(transactionService).addTransaction(any(Transaction.class));
    }

    @Test
    void testGetTransactionById() throws Exception {
        UUID transactionId = UUID.randomUUID();
        Transaction transaction = new Transaction();
        when(transactionService.getTransactionById(transactionId)).thenReturn(transaction);

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(transactionService).getTransactionById(transactionId);
    }

    @Test
    void testUpdateTransaction() throws Exception {
        UUID transactionId = UUID.randomUUID();
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setAmount(100);

        mockMvc.perform(MockMvcRequestBuilders.put("/transactions/{id}", transactionId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}")) // Replace with the JSON representation of your transactionDTO
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(transactionService).updateTransaction(eq(transactionId), eq(transactionDTO));
    }

    @Test
    void testHandleException() throws Exception {
        Exception exception = new IllegalArgumentException("Test exception");

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value(exception.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.exceptionType").value(exception.getClass().getName()));

        verify(transactionService).getAllTransactions(eq(0), eq(100), isNull(), isNull(), isNull(), isNull(), isNull(), isNull(), isNull());
    }
}
