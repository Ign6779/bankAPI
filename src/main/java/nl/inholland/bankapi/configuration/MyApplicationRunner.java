package nl.inholland.bankapi.configuration;

import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.User;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<User> users =
                Arrays.asList(
                        new User()
                );

        List<BankAccount> bankAccounts =
                Arrays.asList(

                );

        List<Transaction> transactions =
                Arrays.asList(

                );
    }
}
