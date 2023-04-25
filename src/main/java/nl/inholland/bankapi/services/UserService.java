package nl.inholland.bankapi.services;

import nl.inholland.bankapi.models.User;
import nl.inholland.bankapi.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return this.userRepository.addUser(user);
    }

    public void updateUser(User user) {
        this.userRepository.updateUser(user);
    }

    public void deleteUser(User user) {
        this.userRepository.deleteUser(user);
    }
}
