package nl.inholland.bankapi.repositories;

import nl.inholland.bankapi.models.UserTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<UserTest, UUID> {

}
