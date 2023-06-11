package nl.inholland.bankapi.cucumber.register;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import nl.inholland.bankapi.configuration.SSLUtils;
import nl.inholland.bankapi.cucumber.BaseStepDefinitions;
import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.models.dto.LoginDTO;
import nl.inholland.bankapi.models.dto.RegisterDTO;
import nl.inholland.bankapi.models.dto.TokenDTO;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@Log
public class RegisterStepDefinitions extends BaseStepDefinitions {
    private RegisterDTO registerDTO;
    private String token;
    private final HttpHeaders httpHeaders = new HttpHeaders();
    private ResponseEntity<String> response;

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    @Before
    public void init() {
        SSLUtils.turnOffSslChecking();
        log.info("Turned off SSL checking");
    }

    @Given("I have a valid register object with valid first name {string} and last name {string} and email {string} and phone {string} and password {string}")
    public void iHaveAValidRegisterObjectWithUserAndPassword(String firstName, String lastName, String email, String phone, String password) {
            registerDTO = new RegisterDTO(firstName, lastName,email,phone,password);
    }

    @When("I call the application register endpoint")
    public void iCallTheApplicationRegisterEndpoint() throws JsonProcessingException {
        httpHeaders.add("Content-Type", "application/json");
        response = restTemplate.exchange(
                "/auth/register",
                HttpMethod.POST,
                new HttpEntity<>(
                        objectMapper.writeValueAsString(registerDTO),
                        httpHeaders), String.class
        );
    }

    @Then("I receive a user")
    public void iReceiveAToken() throws JsonProcessingException {
        Assertions.assertNotNull(objectMapper.readValue(response.getBody(), RegisterDTO.class));
    }
}
