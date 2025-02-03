package com.example.entity;

import com.example.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String verificationCode;
    private boolean verified;
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Transaction> transactions = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TokenStatistics> tokenStatistics = new ArrayList<>();
    private LocalDate creationDate = LocalDate.now();

    public User() {

    }

    public User(String username, String email, String password,String verificationCode) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = Role.USER;
        this.verificationCode = verificationCode;
        this.verified = false;
        this.creationDate = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
