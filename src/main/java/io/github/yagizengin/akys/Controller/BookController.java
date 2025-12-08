package io.github.yagizengin.akys.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.yagizengin.akys.Model.Book;
import io.github.yagizengin.akys.Repository.BookRepository;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/book")
public class BookController {
    private final BookRepository bookRepository;

    public BookController(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @GetMapping("/getAll")
    public List<Book> getAll() {
        return bookRepository.findAll();
    }

    @GetMapping("/getByTitle")
    public ResponseEntity<Book> getByTitle(@RequestParam String title) {
        return bookRepository.findByTitle(title).map(ResponseEntity::ok).orElseThrow(() -> new RuntimeException("Book not found"));
    }
    
    
}
