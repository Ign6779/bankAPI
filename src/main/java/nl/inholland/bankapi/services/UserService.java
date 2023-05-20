package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import nl.inholland.bankapi.models.UserTest;
import nl.inholland.bankapi.models.dto.UserDTO;
import nl.inholland.bankapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserTest getUserByFirstNameAndLastName(String firstName, String lastName){
        return userRepository.findUserTestByFirstNameAndLastName(firstName, lastName).orElseThrow(() -> new EntityNotFoundException("User with: " + firstName+ " "+ lastName + " not found"));
    }

    public List<UserDTO> getAllUsers(Integer offset, Integer limit, Boolean hasAccount){
        List<UserTest> allUsers = (List<UserTest>) userRepository.findAll();
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
        UserTest user=userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        UserDTO dto = mapDtoToUser(user);
        return dto;
    }

    public UserDTO getUserByEmail(String email){
        UserTest user = userRepository.findUserTestByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found"));
        UserDTO dto= mapDtoToUser(user);
        return dto;
    }
    public void addUser(UserTest userTest) {
        userRepository.save(userTest);
    }

    public void updateUser(UUID id, UserTest user) {
        UserTest userToUpdate= userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User with id: " + id + " not found"));
        userToUpdate.setFirstName(user.getFirstName());
        userToUpdate.setPhone(user.getPhone());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setDayLimit(user.getDayLimit());
        userToUpdate.setTransactionLimit(user.getTransactionLimit());
        userToUpdate.setRoles(user.getRoles());
        userToUpdate.setPassword(user.getPassword());
        userRepository.save(userToUpdate);
    }

//    public void updateUser(String email,UserTest user) {
//        UserTest userToUpdate= userRepository.findUserTestByEmail(email).orElseThrow(() -> new EntityNotFoundException("User with id: " + email + " not found"));
//        userToUpdate.setFirstName(user.getFirstName());
//        userToUpdate.setPhone(user.getPhone());
//        userToUpdate.setEmail(user.getEmail());
//        userToUpdate.setDayLimit(user.getDayLimit());
//        userToUpdate.setTransactionLimit(user.getTransactionLimit());
//        userToUpdate.setRoles(user.getRoles());
//        userToUpdate.setPassword(user.getPassword());
//        userRepository.save(userToUpdate);
//    }
//    @Transactional
    public void deleteUser(UUID id) {
        userRepository.deleteById(id); //we could instead pass the entire user object, its the same
    }
//    @Transactional
//    public void deleteUser(String email){
//        userRepository.deleteByEmail(email);
//    }

    private UserDTO mapDtoToUser(UserTest user) {
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
