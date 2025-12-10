package io.github.yagizengin.akys.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.yagizengin.akys.Model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>{
    Optional<Author> findByName(String name);
}