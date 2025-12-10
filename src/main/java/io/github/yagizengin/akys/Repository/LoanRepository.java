package io.github.yagizengin.akys.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.yagizengin.akys.Model.Loan;
import io.github.yagizengin.akys.Model.Loan.Status;
import io.github.yagizengin.akys.Model.User;
import io.github.yagizengin.akys.Model.Book;

import java.util.List;
import java.util.Optional;


public interface LoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findByStatus(Status status);
    List<Loan> findByStatusIn(List<Status> status);
    List<Loan> findByUserAndStatusIn(User user, List<Status> statuses);
    Optional<Loan> findByUserAndBookAndStatusIn(User user, Book book, List<Status> statuses);
}

