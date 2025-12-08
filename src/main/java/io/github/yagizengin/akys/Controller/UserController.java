package io.github.yagizengin.akys.Controller;

import io.github.yagizengin.akys.Model.User;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.yagizengin.akys.Repository.UserRepository;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/user")
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public User create(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/getAll")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/getByEmail")
    public ResponseEntity<User> getByEmail(@RequestParam String email) {
        return userRepository.findByEmail(email).map(ResponseEntity::ok).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
