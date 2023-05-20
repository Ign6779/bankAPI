package nl.inholland.bankapi.repositories;

import nl.inholland.bankapi.models.UserTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserTest, UUID> {
    UserTest findUserTestByFirstName(String name);
    Optional<UserTest>  findUserTestByFirstNameAndLastName(String firstName, String lastName);
    Optional< UserTest> findUserTestById(long id);
    Optional<UserTest> findUserTestByEmail(String email);
    void deleteByEmail(String email);
}
