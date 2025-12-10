package io.github.yagizengin.akys.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.yagizengin.akys.Model.Loan;
import io.github.yagizengin.akys.Model.Loan.Status;

import java.util.Collection;
import java.util.List;


public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByStatus(Status status);
    List<Loan> findByStatusIn(List<Status> status);
}

