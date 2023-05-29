package nl.inholland.bankapi.configuration;

import jakarta.transaction.Transactional;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Role;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.repositories.BankAccountRepository;
import nl.inholland.bankapi.repositories.TransactionRepository;
import nl.inholland.bankapi.repositories.UserRepository;
import nl.inholland.bankapi.services.BankAccountService;
import nl.inholland.bankapi.services.UserService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    private final UserService userService;
    private final BankAccountRepository bankAccountRepository;
    private final TransactionRepository transactionRepository;
    private final BankAccountService bankAccountService;

    public MyApplicationRunner( BankAccountRepository bankAccountRepository, TransactionRepository transactionRepository , UserService userService,BankAccountService bankAccountService) {
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.userService=userService;
        this.bankAccountService=bankAccountService;
    }



    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        List.of(
                new User("user1@email.com", "test", "Dude", "Bli", "+31000000000", 99.9, 99.9, List.of( Role.ROLE_EMPLOYEE)),
                new User("user2@email.com","test" , "Lebowski", "de blo","+31000000001", 99.9, 99.9, List.of(Role.ROLE_CUSTOMER)),
                new User("user3@email.com","test" ,"Maude", "kliblo" , "+31000000002", 99.9, 99.9, List.of(Role.ROLE_CUSTOMER)),
                new User("user4@email.com", "test","test","boss" ,"+31000000002", 99.9, 99.9, List.of(Role.ROLE_CUSTOMER))

        ).forEach(user -> userService.addUser(user));

        userService.getAllUsers(null,null, null).forEach(System.out::println);


//        bankAccountRepository.saveAll(List.of(
//                new BankAccount(userService.getUserByFirstNameAndLastName("Dude" , "Bli"), 100, 80.9, BankAccount.AccountType.CURRENT),
//                new BankAccount(userService.getUserByFirstNameAndLastName("Dude", "Bli"), 80.9, 1100.0, BankAccount.AccountType.SAVINGS),
//                new BankAccount(userService.getUserByFirstNameAndLastName("Lebowski", "de blo"), 80.9, 1000.0, BankAccount.AccountType.CURRENT),
//                new BankAccount(userService.getUserByFirstNameAndLastName("Lebowski" , "de blo"), 80.9, 1000.0, BankAccount.AccountType.SAVINGS),
//                new BankAccount(userService.getUserByFirstNameAndLastName("Maude" , "kliblo"), 80.9, 1000.0, BankAccount.AccountType.CURRENT),
//                new BankAccount(userService.getUserByFirstNameAndLastName("Maude", "kliblo"), 80.9, 1000.0, BankAccount.AccountType.SAVINGS)
//        ));
        List.of(
                new BankAccount(userService.getUserByFirstNameAndLastName("Dude" , "Bli"), 100, 80.9, BankAccount.AccountType.CURRENT),
                new BankAccount(userService.getUserByFirstNameAndLastName("Dude", "Bli"), 80.9, 1100.0, BankAccount.AccountType.SAVINGS),
                new BankAccount(userService.getUserByFirstNameAndLastName("Lebowski", "de blo"), 80.9, 1000.0, BankAccount.AccountType.CURRENT),
                new BankAccount(userService.getUserByFirstNameAndLastName("Lebowski" , "de blo"), 80.9, 1000.0, BankAccount.AccountType.SAVINGS),
                new BankAccount(userService.getUserByFirstNameAndLastName("Maude" , "kliblo"), 80.9, 1000.0, BankAccount.AccountType.CURRENT),
                new BankAccount(userService.getUserByFirstNameAndLastName("Maude", "kliblo"), 80.9, 1000.0, BankAccount.AccountType.SAVINGS)
        ).forEach(bankAccount -> bankAccountService.addBankAccount(bankAccount));

        bankAccountRepository.findAll().forEach(System.out::println);

        List<BankAccount> bankAccounts = (List<BankAccount>) bankAccountRepository.findAll();
        transactionRepository.saveAll(List.of(
                new Transaction(LocalDate.of(2023, 1, 1), bankAccounts.get(0), bankAccounts.get(1), 100.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002")
        ));
        transactionRepository.findAll().forEach(System.out::println);
    }
}
