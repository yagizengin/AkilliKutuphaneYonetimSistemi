package io.github.yagizengin.akys.Controller;

import io.github.yagizengin.akys.Model.Reservation;
import io.github.yagizengin.akys.Repository.ReservationRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationRepository reservationRepository;

    public ReservationController(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    @PostMapping("/create")
    public Reservation create(@RequestBody Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @GetMapping("/getAll")
    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }
}

