package io.github.yagizengin.akys.Controller;

import io.github.yagizengin.akys.Model.Author;
import io.github.yagizengin.akys.Model.Book;
import io.github.yagizengin.akys.Model.Category;
import io.github.yagizengin.akys.Model.Fine;
import io.github.yagizengin.akys.Model.Loan;
import io.github.yagizengin.akys.Model.Reservation;
import io.github.yagizengin.akys.Model.User;
import io.github.yagizengin.akys.Repository.AuthorRepository;
import io.github.yagizengin.akys.Repository.BookRepository;
import io.github.yagizengin.akys.Repository.CategoryRepository;
import io.github.yagizengin.akys.Repository.FineRepository;
import io.github.yagizengin.akys.Repository.LoanRepository;
import io.github.yagizengin.akys.Repository.ReservationRepository;
import io.github.yagizengin.akys.Repository.UserRepository;
import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;
    private final ReservationRepository reservationRepository;
    private final FineRepository fineRepository;
    private final org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    public AdminController(
            AuthorRepository authorRepository,
            BookRepository bookRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository,
            LoanRepository loanRepository,
            ReservationRepository reservationRepository,
            FineRepository fineRepository,
            org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
        this.reservationRepository = reservationRepository;
        this.fineRepository = fineRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/admin")
    public String adminDashboard(Model model) {
        model.addAttribute("bookCount", bookRepository.count());
        model.addAttribute("authorCount", authorRepository.count());
        model.addAttribute("categoryCount", categoryRepository.count());
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("loanCount", loanRepository.count());
        model.addAttribute("reservationCount", reservationRepository.count());
        model.addAttribute("fineCount", fineRepository.count());
        return "admin/dashboard";
    }

    @GetMapping("/admin/books")
    public String books(Model model, Principal principal) {
        Optional<User> me = currentUser(principal);
        boolean isAdmin = me.isPresent() && me.get().getRole() == User.Role.ADMIN;
        
        model.addAttribute("books", bookRepository.findAll());
        
        if (isAdmin) {
            model.addAttribute("authors", authorRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("bookForm", new Book());
            return "admin/books";
        } else {
            return "user/books";
        }
    }

    @PostMapping("/admin/books")
    public String createBook(@ModelAttribute Book book,
                             @RequestParam(required = false) Long authorId,
                             @RequestParam(required = false) Long categoryId) {
        attachAuthorAndCategory(book, authorId, categoryId);
        if (book.getAddedDate() == null) {
            book.setAddedDate(LocalDate.now());
        }
        bookRepository.save(book);
        return "redirect:/admin/books";
    }

    @PostMapping("/admin/books/update")
    public String updateBook(@RequestParam Long id,
                             @RequestParam String title,
                             @RequestParam String isbn,
                             @RequestParam(required = false) String description,
                             @RequestParam(required = false) String coverImageUrl,
                             @RequestParam int totalCopies,
                             @RequestParam int availableCopies,
                             @RequestParam(required = false) Long authorId,
                             @RequestParam(required = false) Long categoryId) {
        bookRepository.findById(id).ifPresent(b -> {
            b.setTitle(title);
            b.setIsbn(isbn);
            b.setDescription(description);
            b.setCoverImageUrl(coverImageUrl);
            b.setTotalCopies(totalCopies);
            b.setAvailableCopies(availableCopies);
            if (authorId != null || categoryId != null) {
                b.setAuthors(new HashSet<>());
                b.setCategories(new HashSet<>());
                attachAuthorAndCategory(b, authorId, categoryId);
            }
            bookRepository.save(b);
        });
        return "redirect:/admin/books";
    }

    @PostMapping("/admin/books/delete")
    public String deleteBook(@RequestParam Long id) {
        bookRepository.deleteById(id);
        return "redirect:/admin/books";
    }

    private void attachAuthorAndCategory(Book book, Long authorId, Long categoryId) {
        if (authorId != null) {
            authorRepository.findById(authorId).ifPresent(author -> {
                book.setAuthors(new HashSet<>());
                book.getAuthors().add(author);
            });
        }
        if (categoryId != null) {
            categoryRepository.findById(categoryId).ifPresent(category -> {
                book.setCategories(new HashSet<>());
                book.getCategories().add(category);
            });
        }
    }

    @GetMapping("/admin/authors")
    public String authors(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("authorForm", new Author());
        return "admin/authors";
    }

    @PostMapping("/admin/authors")
    public String createAuthor(@ModelAttribute Author author) {
        authorRepository.save(author);
        return "redirect:/admin/authors";
    }

    @PostMapping("/admin/authors/update")
    public String updateAuthor(@RequestParam Long id,
                               @RequestParam String name,
                               @RequestParam(required = false) String bio) {
        authorRepository.findById(id).ifPresent(a -> {
            a.setName(name);
            a.setBio(bio);
            authorRepository.save(a);
        });
        return "redirect:/admin/authors";
    }

    @PostMapping("/admin/authors/delete")
    public String deleteAuthor(@RequestParam Long id) {
        authorRepository.deleteById(id);
        return "redirect:/admin/authors";
    }

    @GetMapping("/admin/categories")
    public String categories(Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("categoryForm", new Category());
        return "admin/categories";
    }

    @PostMapping("/admin/categories")
    public String createCategory(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/update")
    public String updateCategory(@RequestParam Long id,
                                 @RequestParam String name) {
        categoryRepository.findById(id).ifPresent(c -> {
            c.setName(name);
            categoryRepository.save(c);
        });
        return "redirect:/admin/categories";
    }

    @PostMapping("/admin/categories/delete")
    public String deleteCategory(@RequestParam Long id) {
        categoryRepository.deleteById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/users")
    public String users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("userForm", new User());
        model.addAttribute("roles", User.Role.values());
        model.addAttribute("statuses", User.AccountStatus.values());
        return "admin/users";
    }

    @PostMapping("/admin/users")
    public String createUser(@ModelAttribute User user) {
        if (user.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userRepository.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/update")
    public String updateUser(@RequestParam Long id,
                             @RequestParam String fullName,
                             @RequestParam String email,
                             @RequestParam(required = false) String phone,
                             @RequestParam User.Role role,
                             @RequestParam User.AccountStatus accountStatus,
                             @RequestParam(required = false) String password) {
        userRepository.findById(id).ifPresent(u -> {
            u.setFullName(fullName);
            u.setEmail(email);
            u.setPhone((phone != null && !phone.isBlank()) ? phone : null);
            u.setRole(role);
            u.setAccountStatus(accountStatus);
            if (password != null && !password.isBlank()) {
                u.setPassword(passwordEncoder.encode(password));
            }
            userRepository.save(u);
        });
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/delete")
    public String deleteUser(@RequestParam Long id) {
        userRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/loans")
    public String loans(Model model) {
        model.addAttribute("loans", loanRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("loanForm", new Loan());
        model.addAttribute("loanStatuses", Loan.Status.values());
        return "admin/loans";
    }

    @PostMapping("/admin/loans")
    public String createLoan(@RequestParam Long userId,
                             @RequestParam Long bookId,
                             @RequestParam LocalDate checkoutDate,
                             @RequestParam LocalDate dueDate,
                             @RequestParam Loan.Status status) {
        Loan loan = new Loan();
        userRepository.findById(userId).ifPresent(loan::setUser);
        bookRepository.findById(bookId).ifPresent(loan::setBook);
        loan.setCheckoutDate(checkoutDate);
        loan.setDueDate(dueDate);
        loan.setStatus(status);
        loanRepository.save(loan);
        return "redirect:/admin/loans";
    }

    @PostMapping("/admin/loans/update")
    public String updateLoan(@RequestParam Long id,
                             @RequestParam Long userId,
                             @RequestParam Long bookId,
                             @RequestParam LocalDate checkoutDate,
                             @RequestParam LocalDate dueDate,
                             @RequestParam(required = false) LocalDate returnDate,
                             @RequestParam Loan.Status status) {
        loanRepository.findById(id).ifPresent(l -> {
            userRepository.findById(userId).ifPresent(l::setUser);
            bookRepository.findById(bookId).ifPresent(l::setBook);
            l.setCheckoutDate(checkoutDate);
            l.setDueDate(dueDate);
            l.setReturnDate(returnDate);
            l.setStatus(status);
            loanRepository.save(l);
        });
        return "redirect:/admin/loans";
    }

    @PostMapping("/admin/loans/delete")
    public String deleteLoan(@RequestParam Long id) {
        loanRepository.deleteById(id);
        return "redirect:/admin/loans";
    }

    @GetMapping("/admin/reservations")
    public String reservations(Model model) {
        model.addAttribute("reservations", reservationRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        model.addAttribute("books", bookRepository.findAll());
        model.addAttribute("reservationStatuses", Reservation.Status.values());
        return "admin/reservations";
    }

    @PostMapping("/admin/reservations")
    public String createReservation(@RequestParam Long userId,
                                    @RequestParam Long bookId,
                                    @RequestParam LocalDate reservationDate,
                                    @RequestParam Reservation.Status status) {
        Reservation reservation = new Reservation();
        userRepository.findById(userId).ifPresent(reservation::setUser);
        bookRepository.findById(bookId).ifPresent(reservation::setBook);
        reservation.setReservationDate(reservationDate);
        reservation.setStatus(status);
        reservationRepository.save(reservation);
        return "redirect:/admin/reservations";
    }

    @PostMapping("/admin/reservations/update")
    public String updateReservation(@RequestParam Long id,
                                    @RequestParam Long userId,
                                    @RequestParam Long bookId,
                                    @RequestParam LocalDate reservationDate,
                                    @RequestParam Reservation.Status status) {
        reservationRepository.findById(id).ifPresent(r -> {
            userRepository.findById(userId).ifPresent(r::setUser);
            bookRepository.findById(bookId).ifPresent(r::setBook);
            r.setReservationDate(reservationDate);
            r.setStatus(status);
            reservationRepository.save(r);
        });
        return "redirect:/admin/reservations";
    }

    @PostMapping("/admin/reservations/delete")
    public String deleteReservation(@RequestParam Long id) {
        reservationRepository.deleteById(id);
        return "redirect:/admin/reservations";
    }

    @GetMapping("/admin/fines")
    public String fines(Model model) {
        model.addAttribute("fines", fineRepository.findAll());
        model.addAttribute("loans", loanRepository.findAll());
        model.addAttribute("fineStatuses", Fine.PaymentStatus.values());
        return "admin/fines";
    }

    @PostMapping("/admin/fines")
    public String createFine(@RequestParam Long loanId,
                             @RequestParam BigDecimal amount,
                             @RequestParam String reason,
                             @RequestParam Fine.PaymentStatus paymentStatus) {
        Fine fine = new Fine();
        loanRepository.findById(loanId).ifPresent(fine::setLoan);
        fine.setAmount(amount);
        fine.setReason(reason);
        fine.setPaymentStatus(paymentStatus);
        fineRepository.save(fine);
        return "redirect:/admin/fines";
    }

    private Optional<User> currentUser(Principal principal) {
        if (principal == null) {
            return Optional.empty();
        }
        return userRepository.findByEmail(principal.getName());
    }
}
