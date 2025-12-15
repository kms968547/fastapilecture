package com.minsu.webservice.domain.user;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "`User`")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    private UserGender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Column(unique = true, length = 20)
    private String phonenumber;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected User() {}

    public User(String email, String password, String name, LocalDate birthdate, UserGender gender, UserRole role, String phonenumber, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.birthdate = birthdate;
        this.gender = gender;
        this.role = role;
        this.phonenumber = phonenumber;
        this.address = address;
        this.active =true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getName() { return name; }
    public LocalDate getBirthdate() { return birthdate; }
    public UserGender getGender() { return gender; }
    public UserRole getRole() { return role; }
    public String getPhonenumber() { return phonenumber; }
    public String getAddress() { return address; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
   
    public void update(String name, LocalDate birthdate, UserGender gender, String address, String phonenumber) {
        if (name != null) this.name = name;
        if (birthdate != null) this.birthdate = birthdate;
        if (gender != null) this.gender = gender;
        if (address != null) this.address = address;
        if (phonenumber != null) this.phonenumber = phonenumber;
        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() { this.active = false; }

    public void changeRole(UserRole role) {
        this.role = role;
        this.updatedAt = LocalDateTime.now();
    }
}
