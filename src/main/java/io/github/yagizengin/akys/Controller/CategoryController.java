package io.github.yagizengin.akys.Controller;

import io.github.yagizengin.akys.Model.Category;
import io.github.yagizengin.akys.Repository.CategoryRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @PostMapping("/create")
    public Category create(@RequestBody Category category) {
        return categoryRepository.save(category);
    }

    @GetMapping("/getAll")
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @GetMapping("/getByName")
    public ResponseEntity<Category> getByName(@RequestParam String name) {
        return categoryRepository.findByName(name)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }
}

