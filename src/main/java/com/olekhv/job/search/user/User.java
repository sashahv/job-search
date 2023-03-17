package com.olekhv.job.search.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    @Column(name = "date_of_creation")
    private LocalDate dateOfCreation;
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
    private List<Education> educations;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Certificate> certificates;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Skill> skills;
    @ManyToMany(cascade = CascadeType.ALL)
    private List<Language> languages;
    @OneToMany(cascade = CascadeType.ALL)
    private List<User> connections;
}
