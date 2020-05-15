package ru.itis.trello.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.itis.trello.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByConfirmationToken(String token);

    List<User> findUserByNameContaining(String name);
}
