package nl.inholland.bankapi.configuration;

import jakarta.transaction.Transactional;
import nl.inholland.bankapi.models.*;
import nl.inholland.bankapi.repositories.BankAccountRepository;
import nl.inholland.bankapi.repositories.TransactionRepository;
import nl.inholland.bankapi.services.BankAccountService;
import nl.inholland.bankapi.services.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    private final UserService userService;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final BankAccountService bankAccountService;

    public MyApplicationRunner(BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository, UserService userService, BankAccountService bankAccountService) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.bankAccountService = bankAccountService;
    }


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List.of(
                new User("user1@email.com", "test", "Dude", "Bli", "+31000000000", 99.9, 99.9, List.of(Role.ROLE_EMPLOYEE, Role.ROLE_CUSTOMER)),
                new User("user2@email.com", "test", "Lebowski", "de blo", "+31000000001", 99.9, 99.9, List.of(Role.ROLE_CUSTOMER)),

                new User("user3@email.com", "test", "Maude", "kliblo", "+31000000002", 99.9, 99.9, List.of(Role.ROLE_CUSTOMER)),
                new User("user4@email.com", "test", "test", "boss", "+31000000002", 99.9, 99.9, List.of(Role.ROLE_CUSTOMER)),
                new User("user99@email.com", "test", "Deez", "Nuts", "+31000000009", 99.9, 99.9, List.of(Role.ROLE_CUSTOMER))

        ).forEach(user -> userService.addUser(user));

        userService.getAllUsers(0, 100, true).forEach(System.out::println);


//        bankAccountRepository.saveAll(List.of(
//                new BankAccount(userService.getUserByFirstNameAndLastName("Dude" , "Bli"), 100, 80.9, BankAccount.AccountType.CURRENT),
//                new BankAccount(userService.getUserByFirstNameAndLastName("Dude", "Bli"), 80.9, 1100.0, BankAccount.AccountType.SAVINGS),
//                new BankAccount(userService.getUserByFirstNameAndLastName("Lebowski", "de blo"), 80.9, 1000.0, BankAccount.AccountType.CURRENT),
//                new BankAccount(userService.getUserByFirstNameAndLastName("Lebowski" , "de blo"), 80.9, 1000.0, BankAccount.AccountType.SAVINGS),
//                new BankAccount(userService.getUserByFirstNameAndLastName("Maude" , "kliblo"), 80.9, 1000.0, BankAccount.AccountType.CURRENT),
//                new BankAccount(userService.getUserByFirstNameAndLastName("Maude", "kliblo"), 80.9, 1000.0, BankAccount.AccountType.SAVINGS)
//        ));
        List.of(
                new BankAccount(null, 100, 1000000000, AccountType.BANK),
                new BankAccount(userService.getUserByFirstNameAndLastName("Dude", "Bli"), 100, 80.9, AccountType.CURRENT),
                new BankAccount(userService.getUserByFirstNameAndLastName("Dude", "Bli"), 80.9, 1100.0, AccountType.SAVINGS),
                new BankAccount(userService.getUserByFirstNameAndLastName("Lebowski", "de blo"), 80.9, 1000.0, AccountType.CURRENT),
                new BankAccount(userService.getUserByFirstNameAndLastName("Lebowski", "de blo"), 80.9, 1000.0, AccountType.SAVINGS),
                new BankAccount(userService.getUserByFirstNameAndLastName("Maude", "kliblo"), 80.9, 1000.0, AccountType.CURRENT),
                new BankAccount(userService.getUserByFirstNameAndLastName("Maude", "kliblo"), 80.9, 1000.0, AccountType.SAVINGS),
                new BankAccount(userService.getUserByFirstNameAndLastName("Dude", "Bli"), 80.9, 1100.0, AccountType.SAVINGS)

        ).forEach(bankAccount -> bankAccountService.addBankAccount(bankAccount));

        bankAccountRepository.findAll().forEach(System.out::println);

        List<BankAccount> bankAccounts = (List<BankAccount>) bankAccountRepository.findAll();
        transactionRepository.saveAll(List.of(
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 1, 12, 30, 45), bankAccounts.get(0), bankAccounts.get(1), 100.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 2, 12, 30, 45), bankAccounts.get(1), bankAccounts.get(0), 200.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 3, 12, 30, 45), bankAccounts.get(2), bankAccounts.get(1), 300.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 4, 12, 30, 45), bankAccounts.get(3), bankAccounts.get(1), 400.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 5, 12, 30, 45), bankAccounts.get(4), bankAccounts.get(1), 500.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 6, 12, 30, 45), bankAccounts.get(5), bankAccounts.get(1), 600.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),

                new Transaction(LocalDateTime.of(2023, Month.MARCH, 7, 12, 30, 45), bankAccounts.get(5), bankAccounts.get(1), 700.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 8, 12, 30, 45), bankAccounts.get(4), bankAccounts.get(1), 800.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002"),
                new Transaction(LocalDateTime.of(2023, Month.MARCH, 9, 12, 30, 45), bankAccounts.get(3), bankAccounts.get(2), 900.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002")
        ));
        transactionRepository.findAll().forEach(System.out::println);
    }
}
