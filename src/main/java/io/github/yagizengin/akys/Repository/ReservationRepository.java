package io.github.yagizengin.akys.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.yagizengin.akys.Model.Reservation;
import io.github.yagizengin.akys.Model.Reservation.Status;
import io.github.yagizengin.akys.Model.User;
import io.github.yagizengin.akys.Model.Book;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserAndStatus(User user, Status status);
    Optional<Reservation> findByUserAndBookAndStatus(User user, Book book, Status status);
}

