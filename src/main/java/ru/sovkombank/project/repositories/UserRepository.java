package ru.sovkombank.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sovkombank.project.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Ищет пользователя по email
     *
     * @param email электронная почта пользователя
     * @return пользователя
     */
    Optional<User> findUserByEmail(String email);
}
