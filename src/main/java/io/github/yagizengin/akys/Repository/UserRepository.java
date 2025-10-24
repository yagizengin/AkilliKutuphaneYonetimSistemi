package io.github.yagizengin.akys.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.yagizengin.akys.Model.User;


public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
