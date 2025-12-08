package io.github.yagizengin.akys.Repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import io.github.yagizengin.akys.Model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
}

