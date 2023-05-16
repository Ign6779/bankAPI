package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.UserTest;
import nl.inholland.bankapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserTest> getAllUsers(Integer offset, Integer limit, Boolean hasAccount){
        List<UserTest> allUsers = (List<UserTest>) userRepository.findAll();

        // Apply filtering based on the provided parameters
        if (offset != null && offset > 0) {
            allUsers = allUsers.stream()
                    .skip(offset)
                    .collect(Collectors.toList());
        }

        if (limit != null && limit > 0) {
            allUsers = allUsers.stream()
                    .limit(limit)
                    .collect(Collectors.toList());
        }

        if (hasAccount != null && hasAccount==false) {
            allUsers = allUsers.stream()
                    .filter(user -> user.getBankAccounts().isEmpty())
                    .collect(Collectors.toList());
        }

        return allUsers;
    }

    public UserTest getUserById(long id){
        return userRepository.findUserTestById(id);
    }


    public void addUser(UserTest userTest) {
        userRepository.save(userTest);
    }

    public void updateUser(Long id,UserTest user) {
        UserTest userToUpdate= userRepository.findUserTestById(id);
        userToUpdate.setName(user.getName());
        userToUpdate.setPhone(user.getPhone());
        userToUpdate.setEmail(user.getEmail());
        userToUpdate.setDayLimit(user.getDayLimit());
        userToUpdate.setTransactionLimit(user.getTransactionLimit());
        userToUpdate.setRole(user.getRole());
        userRepository.save(userToUpdate);
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id); //we could instead pass the entire user object, its the same
    }
}
