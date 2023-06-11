package nl.inholland.bankapi.controllers;

import nl.inholland.bankapi.models.AccountType;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Role;
import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.models.dto.BankAccountDTO;
import nl.inholland.bankapi.models.dto.SearchDTO;
import nl.inholland.bankapi.services.BankAccountService;
import nl.inholland.bankapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BankAccountControllerTest {
    private MockMvc mockMvc;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private UserService userService;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new BankAccountController(bankAccountService)).build();
    }

    @Test
    void testGetAllBankAccounts() throws Exception {
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
    void testGetBankAccount() throws Exception {
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

    @Test
    void testGetIbanByUserFullName() throws Exception {
        SearchDTO searchDTO = new SearchDTO("AA", "BB");
        User user = new User("johndoe@email.com", "test", "AA", "BB", "+31000000000", 99.9, 99.9, List.of(Role.ROLE_CUSTOMER));
        List<BankAccount> accountList = new ArrayList<>();

        BankAccount bankAccount1 = new BankAccount(user, 100, 80.9, AccountType.CURRENT);
        accountList.add(bankAccount1);

        when(bankAccountService.getBankAccountByUserFullName(searchDTO)).thenReturn(accountList);

        mockMvc.perform(MockMvcRequestBuilders.post("/bankAccounts/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"AA\",\"lastName\":\"BB\"}")) // Corrected syntax: Added closing double quote after BB
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].iban").value(bankAccount1.getIban())); // Adjusted JSON path to access the first object in the array

        verify(bankAccountService, times(1)).getBankAccountByUserFullName(searchDTO);
        verifyNoMoreInteractions(bankAccountService);
    }

    @Test
    void testAddBankAccount() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User("johndoe@email.com", "test", "AA", "BB", "+31000000000", 99.9, 99.9, List.of(Role.ROLE_CUSTOMER));
        user.setId(userId);
        BankAccount bankAccount1 = new BankAccount(user, 1, 1, AccountType.CURRENT);

        when(bankAccountService.addBankAccount(any(BankAccount.class))).thenReturn(bankAccount1);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/bankAccounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"userId\": \"" + user.getId() + "\", \"absoluteLimit\": \"1\", \"balance\": \"1\", \"type\": \"CURRENT\"}"))
                .andExpect(status().isCreated());

    }

    @Test
    void testUpdateBankAccount() throws Exception {
        String iban = generateIban();
        User user = new User("johndoe@email.com", "test", "AA", "BB", "+31000000000", 99.9, 99.9, List.of(Role.ROLE_CUSTOMER));
        BankAccount bankAccount = new BankAccount(user, 1, 1, AccountType.CURRENT);

        when(bankAccountService.updateBankAccount(iban, bankAccount, false)).thenReturn(bankAccount);
        mockMvc.perform(MockMvcRequestBuilders.put("/bankAccounts/{iban}", iban)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"absoluteLimit\": \"1\", \"available\": \"true\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    private BankAccount mapDTOToBankAccount(BankAccountDTO dto) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(dto.getBalance());
        bankAccount.setAvailable(true);
        bankAccount.setType(dto.getType());
        bankAccount.setAbsoluteLimit(dto.getAbsoluteLimit());
        return bankAccount;
    }

    public String generateIban() {
        String countryCode = "NL";
        String bankCode = "INHO0";
        String accountNumber = generateRandomAccountNumber();
        return countryCode + bankCode + accountNumber;
    }

}
