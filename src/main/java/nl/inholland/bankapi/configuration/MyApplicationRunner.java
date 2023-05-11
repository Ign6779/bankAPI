package nl.inholland.bankapi.configuration;

import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Transaction;
import nl.inholland.bankapi.models.UserTest;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<UserTest> userTests =
                Arrays.asList(
                        new UserTest()
                );

        List<BankAccount> bankAccounts =
                Arrays.asList(

                );

        List<Transaction> transactions =
                Arrays.asList(

                );
    }
}
