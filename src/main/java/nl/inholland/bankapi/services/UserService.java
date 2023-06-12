package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.Role;
import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.models.dto.RegisterDTO;
import nl.inholland.bankapi.models.dto.UserDTO;
import nl.inholland.bankapi.repositories.UserRepository;
import nl.inholland.bankapi.util.JwtTokenProvider;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;


@Service
public class UserService {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private UserRepository userRepository;

    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public User getUserByFirstNameAndLastName(String firstName, String lastName) {
        return userRepository.findUserByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> new EntityNotFoundException("User with: " + firstName + " " + lastName + " not found"));
    }

    public List<User> getAllUsers(Integer page, Integer size, Boolean hasAccount) {
        PageRequest pageable = PageRequest.of(page, size);
        if (hasAccount != null && hasAccount == false) {
            return userRepository.findAllByBankAccountsIsNull(pageable).getContent().stream().toList();
        }
        return userRepository.findAll(pageable).getContent().stream().toList();
    }

//    public UserDTO getUserById(UUID id){
//        User user=userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
//        UserDTO dto = mapDtoToUser(user);
//        return dto;
//    }

    public User getUserById(UUID id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        if (authentication.getAuthorities().contains(Role.ROLE_CUSTOMER) && !authentication.getAuthorities().contains(Role.ROLE_EMPLOYEE) && !authentication.getName().equals(user.getEmail())) {
            throw new IllegalStateException("You can not retrieve other users beside yourself.");
        }
        return user;
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with id: " + email + " not found"));
    }


    public User addUser(User user) {
        if (userRepository.findUserByEmail(user.getEmail()).isEmpty()) {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        throw new IllegalArgumentException("Email is already taken. Try a new one.");

    }

    public User updateUser(UUID id, User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        if (authentication.getAuthorities().contains(Role.ROLE_CUSTOMER) && !authentication.getAuthorities().contains(Role.ROLE_EMPLOYEE) && !authentication.getName().equals(userToUpdate.getEmail())) {
            throw new IllegalStateException("You can not update other user.");
        }
        updateUserField(user.getFirstName(), userToUpdate::setFirstName, String.class);
        updateUserField(user.getLastName(), userToUpdate::setLastName, String.class);
        updateUserField(user.getPhone(), userToUpdate::setPhone, String.class);
        updateUserField(user.getEmail(), userToUpdate::setEmail, String.class);
        updateUserField(user.getRoles(), userToUpdate::setRoles, List.class);
        updateUserField(user.getPassword(), userToUpdate::setPassword, String.class);
        if (user.getDayLimit() != null || user.getTransactionLimit() != null) {
            if (!authentication.getAuthorities().contains(Role.ROLE_EMPLOYEE)) {
                throw new IllegalStateException("Only employees are allowed to update transaction limit and day limit.");
            }
            if (user.getDayLimit() < 0 || user.getTransactionLimit() < 0) {
                throw new IllegalArgumentException("Value cannot be negative.");
            }
            updateUserField(user.getDayLimit(), userToUpdate::setDayLimit, Double.class);
            updateUserField(user.getTransactionLimit(), userToUpdate::setTransactionLimit, Double.class);
        }
        return userRepository.save(userToUpdate);
    }

    private <T> void updateUserField(T value, Consumer<T> setter, Class expectedType) {
        if (value != null) {
            if (value.toString().isEmpty()) {
                throw new IllegalArgumentException("Please fill in the required fields and don't leave them empty");
            }
            if (!expectedType.isInstance(value)) {
                throw new IllegalArgumentException("Value should be of type " + expectedType.getSimpleName());
            }
            setter.accept(value);
        }
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        if (user.getBankAccounts() != null && !user.getBankAccounts().isEmpty()) {
            throw new IllegalStateException("Cannot delete user with id: " + id + " as they have associated bank accounts");
        }
        userRepository.deleteById(id); //we could instead pass the entire user object, its the same
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

    public String login(String email, String password) throws Exception {
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new AuthenticationException("Invalid email. Try again") {
        });
        if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return jwtTokenProvider.CreateToken(user.getEmail(), user.getRoles());
        } else {
            throw new javax.naming.AuthenticationException("Invalid password");
        }
    }

    public User register(RegisterDTO dto) {
        return addUser(new User(dto.email(), dto.password(), dto.firstName(), dto.lastName(), dto.phone(), 99.9, 99.9, List.of(Role.ROLE_CUSTOMER)));
    }
}
