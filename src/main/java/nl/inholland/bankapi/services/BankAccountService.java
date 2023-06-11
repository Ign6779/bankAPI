package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.AccountType;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.models.dto.BankAccountDTO;
import nl.inholland.bankapi.models.dto.SearchDTO;
import nl.inholland.bankapi.models.dto.UserDTO;
import nl.inholland.bankapi.repositories.BankAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class BankAccountService {
    @Autowired
    private BankAccountRepository bankAccountRepository;


    private UserService userService;

    public BankAccountService(BankAccountRepository bankAccountRepository, UserService userService) {
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;

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

    public List<BankAccount> getAllBankAccounts(Integer page, Integer size) {
        PageRequest pageable = PageRequest.of(page, size);
        return bankAccountRepository.findAll(pageable).getContent().stream().filter(bankAccount -> bankAccount.getType().equals(AccountType.CURRENT) || bankAccount.getType().equals(AccountType.SAVINGS)).collect(Collectors.toList());
    }

    public BankAccount getBankAccountById(String iban) {
        return bankAccountRepository.findById(iban).orElseThrow(EntityNotFoundException::new);
    }

    public List<BankAccount> getBankAccountByUserFullName(SearchDTO searchDTO) {
        Optional<BankAccount> bankAccounts = bankAccountRepository.findBankAccountByUserFirstNameIgnoreCaseAndUserLastNameIgnoreCaseAndType(searchDTO.firstName(), searchDTO.lastName(), AccountType.CURRENT);
        if (!bankAccounts.isEmpty()) {
            return bankAccounts.stream()
                    .filter(bankAccount -> bankAccount.getType() == AccountType.CURRENT)
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("Bank account with user first name and last name: " + searchDTO.firstName() + " " + searchDTO.lastName() + " not found");
        }
    }

    /*    public BankAccount getBankAccountByUserName(String userName) {
        return bankAccountRepository.findByUserName(userName).orElseThrow(EntityNotFoundException::new);
    }
    public BankAccount getBankAccountByUserId(long userId) {
        return bankAccountRepository.findByUserId(userId).orElseThrow(EntityNotFoundException::new);
    }*/
    public BankAccount addBankAccount(BankAccount bankAccount) {
        String iban;
        do {
            if (bankAccount.getType().equals(AccountType.BANK)) {
                iban = "NL01INHO0000000001";
            } else {
                iban = generateIban();
            }
            bankAccount.setIban(iban);
        } while (!bankAccountRepository.findById(iban).isEmpty());
        return bankAccountRepository.save(bankAccount);
    }

    public BankAccount createBankAccount(BankAccountDTO dto) {
        User user = userService.getUserById(dto.getUserId());
        BankAccount bankAccount = mapDTOToBankAccount(dto);
        bankAccount.setUser(user);
        this.addBankAccount(bankAccount);
        return bankAccount;
    }

    public BankAccount updateBankAccount(String iban, BankAccount bankAccount, Boolean isTransaction) {
        BankAccount bankAccountToUpdate = bankAccountRepository.findById(iban)
                .orElseThrow(() -> new EntityNotFoundException("Bank account with id " + iban + " not found"));
        updateBankAccountField(bankAccount.getAbsoluteLimit(), bankAccountToUpdate::setAbsoluteLimit, Double.class);
        updateBankAccountField(bankAccount.isAvailable(), bankAccountToUpdate::setAvailable, Boolean.class);
        if (isTransaction) {
            updateBankAccountField(bankAccount.getBalance(), bankAccountToUpdate::setBalance, Double.class);
        }
        return bankAccountRepository.save(bankAccountToUpdate);
    }

    private <T> void updateBankAccountField(T value, Consumer<T> setter, Class expectedType) {
        if (value != null) {
            if (value.toString().isEmpty()) {
                throw new IllegalArgumentException("Please fill in the required fields and don't leave them empty");
            }
            if (!expectedType.isInstance(value)) {
                throw new IllegalArgumentException("Value should be of type " + expectedType.getSimpleName());
            }
            setter.accept(value);
        }
    }

    private String generateIban() {
        String countryCode = "NL";
        String bankCode = "INHO0";
        String accountNumber = generateRandomAccountNumber();
        return countryCode + bankCode + accountNumber;
    }

    private User mapUserToDTO(UserDTO userDTO) {
        User user = new User();
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setBankAccounts(userDTO.getBankAccounts());
        user.setPhone(userDTO.getPhone());
        user.setId(userDTO.getId());
        user.setRoles(userDTO.getRoles());
        user.setDayLimit(user.getDayLimit());
        user.setTransactionLimit(userDTO.getTransactionLimit());
        return user;
    }

    public BankAccount mapDTOToBankAccount(BankAccountDTO dto) {
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(dto.getBalance());
        bankAccount.setAvailable(true);
        bankAccount.setType(dto.getType());
        bankAccount.setAbsoluteLimit(dto.getAbsoluteLimit());
        return bankAccount;
    }
}
