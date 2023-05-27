package nl.inholland.bankapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankapiApplication.class, args);
    }

//	@Bean
//	public List<User> users() {
//		return new ArrayList<>(List.of(
//			new User("user1@email.com", "Dude", "+31000000000", 99.9, 99.9, User.Role.EMPLOYEE),
//			new User("user2@email.com", "Lebowski ", "+31000000001", 99.9, 99.9, User.Role.CUSTOMER),
//			new User("user3@email.com", "Maude ", "+31000000002", 99.9, 99.9, User.Role.CUSTOMER)
//		));
//	}
//
//	@Bean
//	public List<BankAccount> bankAccounts() {
//		return new ArrayList<>(List.of(
//			new BankAccount( "NL55RABO6771015777", 100, 80.9,  BankAccount.AccountType.CURRENT),
//			new BankAccount( "NL10RABO5262865534",  80.9, 1100.0, BankAccount.AccountType.SAVINGS),
//			new BankAccount("NL55RABO6771015777",  80.9, 1000.0, BankAccount.AccountType.CURRENT),
//			new BankAccount( "NL55RABO6771015777",  80.9, 1000.0, BankAccount.AccountType.SAVINGS),
//			new BankAccount( "NL55RABO6771015777",  80.9, 1000.0, BankAccount.AccountType.CURRENT),
//			new BankAccount( "NL55RABO6771015777", 80.9, 1000.0, BankAccount.AccountType.SAVINGS)
//		));
//	}
//
//	@Bean
//	public List<Transaction> transactions() {
//		return new ArrayList<>(List.of(
//			new Transaction(1, "b5d6fd24-e379-11ed-b5ea-0242ac120002", LocalDate.of(2023, 1, 1), "NL55RABO6771015777", "NL10RABO5262865534", 100.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002")
//		));
//	}
}
