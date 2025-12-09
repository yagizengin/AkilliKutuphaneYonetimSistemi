package io.github.yagizengin.akys.Controller;

import io.github.yagizengin.akys.Model.Book;
import io.github.yagizengin.akys.Model.Loan;
import io.github.yagizengin.akys.Model.User;
import io.github.yagizengin.akys.Repository.BookRepository;
import io.github.yagizengin.akys.Repository.LoanRepository;
import io.github.yagizengin.akys.Repository.ReservationRepository;
import io.github.yagizengin.akys.Repository.UserRepository;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;

    public ViewController(
            BookRepository bookRepository,
            UserRepository userRepository,
            LoanRepository loanRepository,
            ReservationRepository reservationRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
    }

    @GetMapping("/")
    public String landing(Model model, Principal principal) {
        Optional<User> me = currentUser(principal);
        if(me.isPresent()){
            if (me.get().getRole() == User.Role.ADMIN) return "redirect:/admin";
            return "redirect:/member";
        }

        model.addAttribute("auth", false);
        return "index";
    }

    @GetMapping("/member")
    public String member(Model model, Principal principal) {
        User me = currentUser(principal).orElseThrow();
        model.addAttribute("me", me);
        model.addAttribute("loanCount", loanRepository.findAll().stream().filter(l -> l.getUser() != null && l.getUser().getId().equals(me.getId())).count());
        model.addAttribute("reservationCount", reservationRepository.findAll().stream().filter(r -> r.getUser() != null && r.getUser().getId().equals(me.getId())).count());
        model.addAttribute("books", bookRepository.findAll());
        return "user/dashboard";
    }

    @GetMapping("/view/books")
    public String books(Model model) {
        model.addAttribute("books", bookRepository.findAll());
        return "user/books";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/view/profile")
    public String profile(Model model, Principal principal) {
        User me = currentUser(principal).orElseThrow();
        model.addAttribute("me", me);
        return "user/profile";
    }

    @GetMapping("/view/my-loans")
    public String myLoans(Model model, Principal principal) {
        User me = currentUser(principal).orElseThrow();
        model.addAttribute("loans", loanRepository.findAll().stream().filter(l -> l.getUser() != null && l.getUser().getId().equals(me.getId())).toList());
        return "user/my-loans";
    }

    @GetMapping("/view/my-reservations")
    public String myReservations(Model model, Principal principal) {
        User me = currentUser(principal).orElseThrow();
        model.addAttribute("reservations", reservationRepository.findAll().stream().filter(r -> r.getUser() != null && r.getUser().getId().equals(me.getId())).toList());
        return "user/my-reservations";
    }

    @PostMapping("/view/borrow")
    public String borrow(@RequestParam Long bookId, Principal principal) {
        User me = currentUser(principal).orElseThrow();
        Optional<Book> bookOpt = bookRepository.findById(bookId);
        if (bookOpt.isEmpty()) {
            return "redirect:/view/books?error=notfound";
        }
        Book book = bookOpt.get();
        if (book.getAvailableCopies() <= 0) {
            return "redirect:/view/books?error=notavailable";
        }
        Loan loan = new Loan();
        loan.setUser(me);
        loan.setBook(book);
        loan.setCheckoutDate(LocalDate.now());
        loan.setDueDate(LocalDate.now().plusDays(14));
        loan.setStatus(Loan.Status.ACTIVE);
        loanRepository.save(loan);
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        return "redirect:/view/my-loans";
    }

    @PostMapping("/view/return")
    public String returnBook(@RequestParam Long loanId, Principal principal) {
        User me = currentUser(principal).orElseThrow();
        Optional<Loan> loanOpt = loanRepository.findById(loanId);
        if (loanOpt.isEmpty()) {
            return "redirect:/view/my-loans?error=notfound";
        }
        Loan loan = loanOpt.get();
        if (loan.getUser() == null || !loan.getUser().getId().equals(me.getId())) {
            return "redirect:/view/my-loans?error=forbidden";
        }
        if (loan.getStatus() != Loan.Status.RETURNED) {
            loan.setReturnDate(LocalDate.now());
            loan.setStatus(Loan.Status.RETURNED);
            loanRepository.save(loan);
            if (loan.getBook() != null) {
                Book book = loan.getBook();
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                bookRepository.save(book);
            }
        }
        return "redirect:/view/my-loans";
    }

    private Optional<User> currentUser(Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        return userRepository.findByEmail(principal.getName());
    }
}

