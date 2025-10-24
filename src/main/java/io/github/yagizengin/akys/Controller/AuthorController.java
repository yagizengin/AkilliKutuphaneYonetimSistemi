package io.github.yagizengin.akys.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.yagizengin.akys.Model.Author;
import io.github.yagizengin.akys.Repository.AuthorRepository;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/author")
public class AuthorController {
    private final AuthorRepository authorRepository;
    
    public AuthorController(AuthorRepository authorRepository){
        this.authorRepository = authorRepository;
    }

    @PostMapping("/create")
    public Author create(@RequestBody Author author) {        
        return authorRepository.save(author);
    }
    

    @GetMapping("/getAll")
    public List<Author> getAll() {
        return authorRepository.findAll();
    }

    @GetMapping("/getByName")
    public ResponseEntity<Author> getByName(@RequestParam String name) {
        return authorRepository.findByName(name).map(ResponseEntity::ok).orElseThrow(() -> new RuntimeException("Author not found"));
    }
    
}
