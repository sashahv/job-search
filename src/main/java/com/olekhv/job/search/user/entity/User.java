package com.olekhv.job.search.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column(name = "first_name", length = 80, nullable = false)
    private String firstName;
    @Column(name = "last_name", length = 80, nullable = false)
    private String lastName;
    @Column(name = "phone_number", length = 30)
    private String phoneNumber;
    @Column(name = "country", length = 50)
    private String country;
    @Column(name = "city", length = 50)
    private String city;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Education> educations = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<Certificate> certificates = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Skill> skills = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Language> languages = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> connections = new ArrayList<>();
}
