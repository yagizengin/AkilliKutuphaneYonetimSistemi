package io.github.yagizengin.akys.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookID;

    private String title;
    private String isbn;
    private int publishedYear;
    private int pageCount;

    public Book(){}

    public Long getBookID() {
        return bookID;
    }
    public void setBookID(Long id) {
        this.bookID = id;
    }

    public String getIsbn() {
        return isbn;
    }
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getPublishedYear() {
        return publishedYear;
    }
    public void setPublishedYear(int publishedYear) {
        this.publishedYear = publishedYear;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
