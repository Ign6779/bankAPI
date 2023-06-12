package nl.inholland.bankapi.cucumber.bankAccounts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import nl.inholland.bankapi.configuration.SSLUtils;
import nl.inholland.bankapi.cucumber.BaseStepDefinitions;
import nl.inholland.bankapi.models.BankAccount;
import nl.inholland.bankapi.models.Role;
import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.models.dto.LoginDTO;
import nl.inholland.bankapi.models.dto.TokenDTO;
import nl.inholland.bankapi.services.BankAccountService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@Log
public class BankAccontStepDefinitions extends BaseStepDefinitions {
    HttpHeaders httpHeaders = new HttpHeaders();
    @Autowired
    private TestRestTemplate restTemplate;
    private ResponseEntity<String> response;
    @Autowired
    private ObjectMapper mapper;
    private String token;
    private BankAccountService bankAccountServiceMock;

    @SneakyThrows
    @Before
    public void init() {
        bankAccountServiceMock= mock(BankAccountService.class);
        SSLUtils.turnOffSslChecking();
        log.info("Turned off SSL checking");
    }

    @Given("The endpoint for {string} is available for method {string}")
    public void theEndpointForIsAvailableForMethod(String endpoint, String method) {
        response = restTemplate.exchange(
                "/" + endpoint,
                HttpMethod.OPTIONS,
                new HttpEntity<>(
                        null,
                        httpHeaders),
                String.class);
        List<String> options = Arrays.stream(response.getHeaders()
                .get("Allow")
                .get(0)
                .split(",")).toList();

        Assertions.assertTrue(options.contains(method.toUpperCase()));
    }

    @When("I retrieve all bankAccounts")
    public void iRetrieveAllBankAccounts() {
        httpHeaders.clear();
        httpHeaders.add("Authorization", "Bearer " + token);
        response = restTemplate.exchange(
                "/bankAccounts",
                HttpMethod.GET,
                new HttpEntity<>(null,
                        httpHeaders),
                String.class
        );
    }

    @Then("I get a list of {int} bankAccounts")
    public void iGetAListOfUsers(int expected) {
        String body = (String) response.getBody();
        int actual = JsonPath.read(body, "$.size()");
        Assertions.assertEquals(expected, actual);
    }

    @And("I get http status {int}")
    public void iGetHttpStatus(int status) {
        int actual = response.getStatusCode().value();
        Assertions.assertEquals(status, actual);
    }

    private String getToken(LoginDTO loginDTO) throws JsonProcessingException {
        response = restTemplate
                .exchange("/login",
                        HttpMethod.POST,
                        new HttpEntity<>(mapper.writeValueAsString(loginDTO), httpHeaders), String.class);
        TokenDTO tokenDTO = mapper.readValue(response.getBody(), TokenDTO.class);
        return tokenDTO.token();
    }

    @Given("I log in as employee")
    public void iLogInAsEmployee() throws JsonProcessingException {
        httpHeaders.clear();
        httpHeaders.add("Content-Type", "application/json");
        LoginDTO loginDTO = new LoginDTO(VALID_Employee, VALID_PASSWORD);
        token = getToken(loginDTO);
    }

    @When("I provide a bank account with a user that has {int} and has absoluteLimit {double} and balance {double}")
    public void iProvideBankAccountWithUserThatHasId(int userId, double absoluteLimit, double balance){
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + token);
        response = restTemplate.exchange(
                "/bankAccounts",
                HttpMethod.POST,
                new HttpEntity<>(
                        """
                                {
                                    "userId":"1",
                                	"absoluteLimit": 100.0,
                                	"balance": 0.0,
                                	"type": "CURRENT"
                                }
                                     """, httpHeaders),
                String.class);
    }

    @Then("Then the balance of the bank account is {double}")
    public void theBalanceOfBankAccount(double balance) throws JsonProcessingException {
        String body = response.getBody();
        BankAccount bankAccount = mapper.readValue(body, BankAccount.class);
        double actual = bankAccount.getBalance();
        Assertions.assertEquals(actual, actual);
    }

    @Given("There is a bank account with iban {string} with absolute limit {double} in the system")
    public void createBankAccountInSystem(String iban, double absoluteLimit) {
        BankAccount bankAccount= new BankAccount();
        bankAccount.setIban(iban);
        bankAccount.setAbsoluteLimit(absoluteLimit);
        when(bankAccountServiceMock.addBankAccount(eq(bankAccount))).thenReturn(bankAccount);
    }


    @When("I update the absolute limit of the bank account {string} to {double}")
    public void iUpdateTheAbsoluteLimitOfBankAccount(String iban, Double absoluteLimit){
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + token);
        String requestJson = String.format("{\"absoluteLimit\": \"%s\"}", absoluteLimit);

        response = restTemplate.exchange(
                "/bankAccounts/" + iban,
                HttpMethod.PUT,
                new HttpEntity<>(requestJson, httpHeaders),
                String.class
        );
    }

    @Then("The absolute limit of bank account {string} is {double}")
    public void verifyUserFirstName(String iban, String expectedAbsoluteLimit) {
       BankAccount bankAccount= bankAccountServiceMock.getBankAccountById(iban);
        double actualAbsoluteLimit = bankAccount.getAbsoluteLimit();
        Assertions.assertEquals(expectedAbsoluteLimit, actualAbsoluteLimit);
    }
}
