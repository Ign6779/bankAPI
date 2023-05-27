package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;

import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.models.dto.UserDTO;
import nl.inholland.bankapi.repositories.UserRepository;
import nl.inholland.bankapi.util.JwtTokenProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public UserService(UserRepository userRepository,  JwtTokenProvider jwtTokenProvider,BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.bCryptPasswordEncoder=bCryptPasswordEncoder;
    }




    public User getUserByFirstNameAndLastName(String firstName, String lastName){
        return userRepository.findUserByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> new EntityNotFoundException("User with: " + firstName+ " "+ lastName + " not found"));
    }

    public List<UserDTO> getAllUsers(Integer offset, Integer limit, Boolean hasAccount){
        List<User> allUsers = (List<User>) userRepository.findAll();
        final List<UserDTO> dtos= new ArrayList<>();
        List<UserDTO> users= dtos;
        allUsers.forEach(user -> dtos.add(mapDtoToUser(user)));
        if (offset != null && offset > 0) {
            users = dtos.stream()
                    .skip(offset)
                    .collect(Collectors.toList());
        }

        if (limit != null && limit > 0) {
            users = dtos.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        if (hasAccount != null && hasAccount==false) {
            users = dtos.stream()
                    .filter(user -> user.getBankAccounts().isEmpty())
                    .collect(Collectors.toList());
        }

        return users;
    }

    public UserDTO getUserById(UUID id){
        User user=userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        UserDTO dto = mapDtoToUser(user);
        return dto;
    }

    public UserDTO getUserByEmail(String email){
        UserTest user = userRepository.findUserTestByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found"));
        UserDTO dto= mapDtoToUser(user);
        return dto;
    }
   

    public User addUser(User user) {
        if(userRepository.findUserByEmail(user.getEmail()).isEmpty()){
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        throw new IllegalArgumentException("Email is already taken. Try a new one.");

    }

    public User updateUser(UUID id, User user) {
        User userToUpdate = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        updateUserField(user.getFirstName(), userToUpdate::setFirstName);
        updateUserField(user.getLastName(), userToUpdate::setLastName);
        updateUserField(user.getPhone(), userToUpdate::setPhone);
        updateUserField(user.getEmail(), userToUpdate::setEmail);
        updateUserField(user.getDayLimit(), userToUpdate::setDayLimit);
        updateUserField(user.getTransactionLimit(), userToUpdate::setTransactionLimit);
        updateUserField(user.getRoles(), userToUpdate::setRoles);
        updateUserField(user.getPassword(), userToUpdate::setPassword);

        return userRepository.save(userToUpdate);
    }

    private <T> void updateUserField(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }

    public void deleteUser(UUID id) {
        User user= userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
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

    public String login(String email, String password) throws Exception{
        User user = userRepository.findUserByEmail(email).orElseThrow(() -> new AuthenticationException("User with email: " + email + " not found") {});
        if(bCryptPasswordEncoder.matches(password, user.getPassword())){
            return jwtTokenProvider.CreateToken(user.getEmail(), user.getRoles());
        }
        else {
            throw new javax.naming.AuthenticationException("Invalid username/password");
        }
    }
}
