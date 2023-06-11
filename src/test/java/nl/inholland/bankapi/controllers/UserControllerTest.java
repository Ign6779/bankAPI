package nl.inholland.bankapi.controllers;

import nl.inholland.bankapi.models.Role;
import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.models.dto.UserDTO;
import nl.inholland.bankapi.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(userService)).build();
    }

    @Test
    void testGetAllUsers() throws Exception {
        List<User> users = Arrays.asList(
                new User("john.doe@example.com", "test", "John", "Doe", "123456789", 100.0, 100.0, Arrays.asList(Role.ROLE_CUSTOMER)),
                new User("jane.doe@example.com", "test", "Jane", "Doe", "123456789", 100.0, 100.0, Arrays.asList(Role.ROLE_EMPLOYEE))
        );

        when(userService.getAllUsers(0, 100, false)).thenReturn(users);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(users.size()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(users.get(0).getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value(users.get(1).getEmail()));

        verify(userService, times(1)).getAllUsers(0, 100, false);
        verifyNoMoreInteractions(userService);
    }


    @Test
    void testGetUserById() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User("john.doe@example.com", "Doe", "John", "Doe ", "123456789", 100.0, 100.0, Arrays.asList(Role.ROLE_CUSTOMER));
        user.setId(userId);
        when(userService.getUserById(userId)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()));

        verify(userService, times(1)).getUserById(userId);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testGetUserByEmail() throws Exception {
        UUID userId = UUID.randomUUID();
        String email = "john.doe@example.com";
        User user = new User(email, "Doe", "John", "Doe ", "123456789", 100.0, 100.0, Arrays.asList(Role.ROLE_CUSTOMER));
        user.setId(userId);
        when(userService.getUserByEmail(email)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/email/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()));

        verify(userService, times(1)).getUserByEmail(email);
        verifyNoMoreInteractions(userService);
    }

    @Test
    void testCreateUser() throws Exception {
        User user = new User("john.doe@example.com", "password", "John", "Doe", "123456789", 100.0, 100.0, Arrays.asList(Role.ROLE_CUSTOMER));
        when(userService.addUser(user)).thenReturn(user);
        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john.doe@example.com\",\"password\":\"password\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"phone\":\"123456789\",\"dayLimit\":100.0,\"transactionLimit\":100.0,\"roles\":[\"ROLE_CUSTOMER\"]}"))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()));

        verify(userService, times(1)).addUser(user);
        verifyNoMoreInteractions(userService);
    }

    /*@Test
    void testUpdateUser() throws Exception {
        UUID userId = UUID.randomUUID();
        User user = new User("john.doe@example.com", "password", "John", "Doe", "123456789", 100.0, 100.0, Arrays.asList(Role.ROLE_CUSTOMER));

//        when(userService.updateUser(userId, user)).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"john.doe@example.com\",\"password\":\"password\",\"firstName\":\"John\",\"lastName\":\"Doe\",\"phone\":\"123456789\",\"dayLimit\":100.0,\"transactionLimit\":100.0,\"roles\":[\"ROLE_CUSTOMER\"]}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(user.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(user.getFirstName()));

//        verify(userService, times(1)).updateUser(userId, user);
        verifyNoMoreInteractions(userService);
    }*/

    @Test
    void testDeleteUser() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
        verifyNoMoreInteractions(userService);
    }

    private UserDTO mapDtoToUser(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRoles(user.getRoles());
        dto.setPhone(user.getPhone());
        dto.setEmail(user.getEmail());
        dto.setDayLimit(user.getDayLimit());
        dto.setTransactionLimit(user.getTransactionLimit());
        dto.setBankAccounts(user.getBankAccounts());
        return dto;
    }
}
