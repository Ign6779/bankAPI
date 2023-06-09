package nl.inholland.bankapi.controllers;

import nl.inholland.bankapi.models.AccountType;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Role;
import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.models.dto.BankAccountDTO;
import nl.inholland.bankapi.models.dto.SearchDTO;
import nl.inholland.bankapi.services.BankAccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

public class BankAccountControllerTest {
    private MockMvc mockMvc;
    @Mock
    private BankAccountService bankAccountService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new BankAccountController(bankAccountService)).build();
    }

    @Test
    void testGetAllBankAccounts() throws Exception{
        List<BankAccountDTO> bankAccountDTOSs = Arrays.asList(
            new BankAccountDTO(UUID.randomUUID(), 100, 80.9, AccountType.CURRENT),
            new BankAccountDTO(UUID.randomUUID(), 50, 40.45, AccountType.CURRENT)
        );

        // Convert BankAccountDTO list to BankAccount list because GetAllBankAccounts return BankAccount instead of BankAccountDTO
        List<BankAccount> bankAccounts = bankAccountDTOSs.stream()
                .map(this::mapDTOToBankAccount)
                .collect(Collectors.toList());

        when(bankAccountService.getAllBankAccounts(0, 100)).thenReturn(bankAccounts);

        mockMvc.perform(MockMvcRequestBuilders.get("/bankAccounts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(bankAccounts.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].absoluteLimit").value(bankAccounts.get(0).getAbsoluteLimit()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].absoluteLimit").value(bankAccounts.get(1).getAbsoluteLimit()));

        verify(bankAccountService, times(1)).getAllBankAccounts(0, 100);
        verifyNoMoreInteractions(bankAccountService);
    }

    @Test
    void testGetBankAccount() throws Exception{
        String iban = generateIban();
        BankAccountDTO bankAccountDTO = new BankAccountDTO(UUID.randomUUID(), 1989, 22.0, AccountType.CURRENT);
        BankAccount bankAccount = mapDTOToBankAccount(bankAccountDTO);
        bankAccount.setIban(iban);

        when(bankAccountService.getBankAccountById(iban)).thenReturn(bankAccount);

        mockMvc.perform(MockMvcRequestBuilders.get("/bankAccounts/{iban}", iban)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.absoluteLimit").value(bankAccount.getAbsoluteLimit()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(bankAccount.getBalance()));

        verify(bankAccountService, times(1)).getBankAccountById(iban);
        verifyNoMoreInteractions(bankAccountService);
    }






















    private BankAccount mapDTOToBankAccount(BankAccountDTO dto) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(dto.getBalance());
        bankAccount.setAvailable(true);
        bankAccount.setType(dto.getType());
        bankAccount.setAbsoluteLimit(dto.getAbsoluteLimit());
        return bankAccount;
    }
    public String generateIban(){
        String countryCode = "NL";
        String bankCode = "INHO0";
        String accountNumber= generateRandomAccountNumber();
        return countryCode+bankCode+accountNumber;
    }

    private static String generateRandomAccountNumber() {
        Random random = new Random();
        StringBuilder accountNumber = new StringBuilder();

        // Generate 9 random digits
        for (int i = 0; i < 9; i++) {
            int digit = random.nextInt(10);
            accountNumber.append(digit);
        }

        return accountNumber.toString();
    }

}
