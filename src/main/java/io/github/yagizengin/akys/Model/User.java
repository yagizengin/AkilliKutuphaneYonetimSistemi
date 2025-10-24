package io.github.yagizengin.akys.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userID;

    private String name;
    private String email;

    public User(){}
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Long getUserID() {
        return userID;
    }
    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        if(email.contains("@")) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

}
