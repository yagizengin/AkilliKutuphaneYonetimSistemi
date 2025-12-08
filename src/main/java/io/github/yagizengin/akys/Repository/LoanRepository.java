package io.github.yagizengin.akys.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.yagizengin.akys.Model.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {
}

