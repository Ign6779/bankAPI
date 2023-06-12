package nl.inholland.bankapi.cucumber.users;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import io.cucumber.java.Before;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import nl.inholland.bankapi.configuration.SSLUtils;
import nl.inholland.bankapi.cucumber.BaseStepDefinitions;
import nl.inholland.bankapi.models.Role;
import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Log
public class UserStepDefinitions extends BaseStepDefinitions {
    HttpHeaders httpHeaders = new HttpHeaders();
    @Autowired
    private TestRestTemplate restTemplate;
    private ResponseEntity<String> response;
    @Autowired
    private ObjectMapper mapper;
    private String token;

    private UserService userServiceMock;
    @SneakyThrows
    @Before
    public void init() {
        userServiceMock = mock(UserService.class);
        SSLUtils.turnOffSslChecking();
        log.info("Turned off SSL checking");
    }


    @When("I retrieve all users")
    public void iRetrieveAllUsers() {
        httpHeaders.clear();
        httpHeaders.add("Authorization", "Bearer " + token);
        response = restTemplate.exchange(
                "/users",
                HttpMethod.GET,
                new HttpEntity<>(null,
                        httpHeaders),
                String.class
        );
    }

    @Then("I get a list of {int} users")
    public void iGetAListOfUsers(int expected) {
        String body = (String) response.getBody();
        int actual = JsonPath.read(body, "$.size()");
        Assertions.assertEquals(expected, actual);
    }

    @Given("There is a user with ID {string} with first name {string} in the system")
    public void createUserInSystem(String userId, String firstName) {
        User user= new User(".@gmail.com","test",firstName,"Doe","090523",100.0,100.0,List.of(Role.ROLE_CUSTOMER));
        user.setId(UUID.fromString(userId));
        when(userServiceMock.addUser(eq(user))).thenReturn(user);
    }

    @When("I update the firstname of the user {string} to {string}")
    public void iUpdateTheUserFirstname(String userId, String newFirstName){
        httpHeaders.add("Content-Type", "application/json");
        httpHeaders.add("Authorization", "Bearer " + token);
        String requestJson = String.format("{\"firstName\": \"%s\"}", newFirstName);

        response = restTemplate.exchange(
                "/users/" + userId,
                HttpMethod.PUT,
                new HttpEntity<>(requestJson, httpHeaders),
                String.class
        );
    }
    @Then("The first name of user {string} is {string}")
    public void verifyUserFirstName(String userId, String expectedFirstName) {
        User user = userServiceMock.getUserById(UUID.fromString(userId));
        String actualFirstName = user.getFirstName();
        Assertions.assertEquals(expectedFirstName, actualFirstName);
    }

    @When("I delete the user with ID {string}")
    public void deleteUserById(String userId) {
        httpHeaders.add("Authorization", "Bearer " + token);
        response = restTemplate.exchange(
                "/users/" + userId,
                HttpMethod.DELETE,
                new HttpEntity<>(null, httpHeaders),
                String.class
        );
    }

    @Then("The user with ID {string} is not found in the system")
    public void verifyUserNotFound(String userId) {
        User user = userServiceMock.getUserById(UUID.fromString(userId));
        Assertions.assertNull(user, "User with ID " + userId + " should not exist");
    }
}
