package io.github.yagizengin.akys.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.yagizengin.akys.Model.Fine;

public interface FineRepository extends JpaRepository<Fine, Long> {
}

