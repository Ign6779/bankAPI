package nl.inholland.bankapi.repositories;

import nl.inholland.bankapi.models.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepository {
    private List<User> users;

    public UserRepository(List<User> users) {
        this.users = users;
    }

    public User addUser(User user) {
        users.add(user);
        return (user); //I believe returning the object is correct REST but IDK tbh
    }

    public void updateUser(User user) { //this is all taken from the ghub, utinam recte operatur
        users.stream()
                .filter(u -> u.equals(user))
                .findFirst()
                .ifPresentOrElse(
                    u -> users.set(users.indexOf(u), user),
                    () -> {
                    throw new IllegalArgumentException("User not present");
                    }
                );
    }

    public void deleteUser(User user) {
        users.stream()
                .filter(u -> u.equals(user))
                .findFirst().ifPresentOrElse(
                        u -> users.remove(u),
                        () -> {
                            throw new IllegalArgumentException("User not present");
                        }
                );
    }
}
