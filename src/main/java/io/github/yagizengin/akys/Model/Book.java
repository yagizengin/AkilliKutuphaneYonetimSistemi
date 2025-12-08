package io.github.yagizengin.akys.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String isbn;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "cover_image_url")
    private String coverImageUrl;

    @Column(nullable = false)
    private int totalCopies;

    @Column(nullable = false)
    private int availableCopies;

    @Column(name = "shelf_location")
    private String shelfLocation;

    @Column(name = "added_date")
    private LocalDate addedDate;

    @ManyToMany
    @JoinTable(
        name = "book_authors",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "book_categories",
        joinColumns = @JoinColumn(name = "book_id"),
        inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @OneToMany(mappedBy = "book")
    private List<Loan> loans = new ArrayList<>();

    @OneToMany(mappedBy = "book")
    private List<Reservation> reservations = new ArrayList<>();

    public Book(){}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }
    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public int getTotalCopies() {
        return totalCopies;
    }
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public String getShelfLocation() {
        return shelfLocation;
    }
    public void setShelfLocation(String shelfLocation) {
        this.shelfLocation = shelfLocation;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }
    public void setAddedDate(LocalDate addedDate) {
        this.addedDate = addedDate;
    }

    public Set<Author> getAuthors() {
        return authors;
    }
    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }

    public Set<Category> getCategories() {
        return categories;
    }
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }

    public List<Loan> getLoans() {
        return loans;
    }
    public void setLoans(List<Loan> loans) {
        this.loans = loans;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }
    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
