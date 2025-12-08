package io.github.yagizengin.akys.Controller;

import io.github.yagizengin.akys.Model.Loan;
import io.github.yagizengin.akys.Repository.LoanRepository;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/loan")
public class LoanController {
    private final LoanRepository loanRepository;

    public LoanController(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;
    }

    @PostMapping("/create")
    public Loan create(@RequestBody Loan loan) {
        return loanRepository.save(loan);
    }

    @GetMapping("/getAll")
    public List<Loan> getAll() {
        return loanRepository.findAll();
    }
}

