package nl.inholland.bankapi;

import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BankapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankapiApplication.class, args);
	}

	@Bean
	public List<User> users() {
		return new ArrayList<>(List.of(
			new User(1, "1cfc38d6-e378-11ed-b5ea-0242ac120002", "user1@email.com", "Dude", "+31000000000", 99.9, 99.9, User.Role.EMPLOYEE),
			new User(2, "1cfc3ea8-e378-11ed-b5ea-0242ac120002", "user2@email.com", "Lebowski ", "+31000000001", 99.9, 99.9, User.Role.CUSTOMER),
			new User(3, "1cfc409c-e378-11ed-b5ea-0242ac120002", "user3@email.com", "Maude ", "+31000000002", 99.9, 99.9, User.Role.CUSTOMER)
		));
	}

	@Bean
	public List<BankAccount> bankAccounts() {
		return new ArrayList<>(List.of(
			new BankAccount(1, "NL55RABO6771015777", "1cfc38d6-e378-11ed-b5ea-0242ac120002", 80.9, 999.0, BankAccount.AccountType.CURRENT),
			new BankAccount(2, "NL10RABO5262865534", "1cfc38d6-e378-11ed-b5ea-0242ac120002", 80.9, 1100.0, BankAccount.AccountType.SAVINGS),
			new BankAccount(3, "NL55RABO6771015777", "1cfc3ea8-e378-11ed-b5ea-0242ac120002", 80.9, 1000.0, BankAccount.AccountType.CURRENT),
			new BankAccount(4, "NL55RABO6771015777", "1cfc3ea8-e378-11ed-b5ea-0242ac120002", 80.9, 1000.0, BankAccount.AccountType.SAVINGS),
			new BankAccount(5, "NL55RABO6771015777", "1cfc409c-e378-11ed-b5ea-0242ac120002", 80.9, 1000.0, BankAccount.AccountType.CURRENT),
			new BankAccount(6, "NL55RABO6771015777", "1cfc409c-e378-11ed-b5ea-0242ac120002", 80.9, 1000.0, BankAccount.AccountType.SAVINGS)
		));
	}

	@Bean
	public List<Transaction> transactions() {
		return new ArrayList<>(List.of(
			new Transaction(1, "b5d6fd24-e379-11ed-b5ea-0242ac120002", LocalDate.of(2023, 1, 1), "NL55RABO6771015777", "NL10RABO5262865534", 100.0, "1cfc38d6-e378-11ed-b5ea-0242ac120002")
		));
	}
}
