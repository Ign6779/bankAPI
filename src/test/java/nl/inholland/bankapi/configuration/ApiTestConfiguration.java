package nl.inholland.bankapi.configuration;

import nl.inholland.bankapi.util.JwtTokenProvider;
import org.springframework.boot.test.mock.mockito.MockBean;

@org.springframework.boot.test.context.TestConfiguration
public class ApiTestConfiguration {

    @MockBean
    private JwtTokenProvider jwtTokenProvider;
}
