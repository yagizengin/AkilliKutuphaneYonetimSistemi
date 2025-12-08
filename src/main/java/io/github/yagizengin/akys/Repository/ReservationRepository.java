package io.github.yagizengin.akys.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import io.github.yagizengin.akys.Model.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}

