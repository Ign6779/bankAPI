package nl.inholland.bankapi.services;

import jakarta.persistence.EntityNotFoundException;
import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService() {

    }

    public List<User> getAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public User getUserById(UUID uuid) {
        return userRepository.findById(uuid).orElseThrow(EntityNotFoundException::new);
    }

    public void addUser(User user) {
        userRepository.save(user);
    }

    public void updateUser(User user) {
        userRepository.save(user); //i know its the same as 'addUser' but it should first search if the user exists
    }

    public void deleteUser(UUID uuid) {
        userRepository.deleteById(uuid); //we could instead pass the entire user object, its the same
    }
}
