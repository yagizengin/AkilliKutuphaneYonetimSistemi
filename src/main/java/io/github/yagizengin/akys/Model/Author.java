package io.github.yagizengin.akys.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorID;
    private String name;

    public Author(){}

    public Author(String name){
        setName(name);
    }

    public Long getAuthorID() {
        return authorID;
    }
    public void setAuthorID(Long authorID) {
        this.authorID = authorID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
