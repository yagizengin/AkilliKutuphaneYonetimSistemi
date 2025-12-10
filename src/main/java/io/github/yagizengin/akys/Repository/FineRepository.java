package io.github.yagizengin.akys.Repository;

import io.github.yagizengin.akys.Model.Fine;
import io.github.yagizengin.akys.Model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByLoan_User(User user);
    List<Fine> findByLoan_UserAndPaymentStatus(User user, Fine.PaymentStatus paymentStatus);
}

