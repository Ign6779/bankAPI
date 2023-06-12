package nl.inholland.bankapi.cucumber;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

import static io.cucumber.core.options.Constants.GLUE_PROPERTY_NAME;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@CucumberContextConfiguration
public class BaseStepDefinitions {
    public static final String VALID_Costumer = "costumer";
    public static final String VALID_Employee = "employee";
    public static final String VALID_PASSWORD = "test";
    public static final String INVALID_USERNAME = "bla";
    public static final String INVALID_PASSWORD = "invalid";

}
