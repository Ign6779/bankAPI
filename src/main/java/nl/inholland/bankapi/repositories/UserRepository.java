package nl.inholland.bankapi.repositories;

import nl.inholland.bankapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByFirstNameAndLastName(String firstName, String lastName);
    Optional<User> findUserByEmail(String email);

}
