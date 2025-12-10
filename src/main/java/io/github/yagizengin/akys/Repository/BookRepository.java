package io.github.yagizengin.akys.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.yagizengin.akys.Model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);
}
