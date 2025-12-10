package io.github.yagizengin.akys.Controller;

import io.github.yagizengin.akys.Config.JwtTokenProvider;
import io.github.yagizengin.akys.Model.User;
import io.github.yagizengin.akys.Repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    public AuthController(UserRepository userRepository, 
                         PasswordEncoder passwordEncoder,
                         JwtTokenProvider tokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest, 
                                                      HttpServletResponse response) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        User user = userOpt.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Map.of("error", "Invalid email or password"));
        }

        if (user.getAccountStatus() != User.AccountStatus.ACTIVE) {
            return ResponseEntity.status(403).body(Map.of("error", "Account is blocked"));
        }

        String token = tokenProvider.generateToken(user.getEmail(), user.getRole().name());

        Cookie cookie = new Cookie("JWT_TOKEN", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); 
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);
        response.addCookie(cookie);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "Login successful");
        responseBody.put("user", Map.of(
            "email", user.getEmail(),
            "fullName", user.getFullName(),
            "role", user.getRole().name()
        ));

        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT_TOKEN", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok(Map.of("message", "Logout successful"));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest registerRequest) {
        if (registerRequest.getFullName() == null || registerRequest.getFullName().trim().isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("error", "Ad soyad gereklidir."));
        }
        
        if (registerRequest.getEmail() == null || registerRequest.getEmail().trim().isEmpty()) {
            return ResponseEntity.status(400).body(Map.of("error", "Email gereklidir."));
        }
        
        if (registerRequest.getPassword() == null || registerRequest.getPassword().length() < 6) {
            return ResponseEntity.status(400).body(Map.of("error", "Parola en az 6 karakter olmalıdır."));
        }
        
        if (!registerRequest.getPassword().equals(registerRequest.getPasswordConfirm())) {
            return ResponseEntity.status(400).body(Map.of("error", "Parolalar eşleşmiyor."));
        }
        
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.status(409).body(Map.of("error", "Bu email adresi zaten kullanılıyor."));
        }
        
        User newUser = new User();
        newUser.setEmail(registerRequest.getEmail().trim().toLowerCase());
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setFullName(registerRequest.getFullName().trim());
        newUser.setPhone(registerRequest.getPhone() != null ? registerRequest.getPhone().trim() : null);
        newUser.setRole(User.Role.MEMBER);
        newUser.setAccountStatus(User.AccountStatus.ACTIVE);
        
        userRepository.save(newUser);
        
        return ResponseEntity.ok(Map.of("message", "Kayıt başarılı! Giriş yapabilirsiniz."));
    }

    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class RegisterRequest {
        private String fullName;
        private String email;
        private String phone;
        private String password;
        private String passwordConfirm;

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPasswordConfirm() {
            return passwordConfirm;
        }

        public void setPasswordConfirm(String passwordConfirm) {
            this.passwordConfirm = passwordConfirm;
        }
    }
}

