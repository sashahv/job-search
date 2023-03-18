package com.olekhv.job.search.user;

import com.olekhv.job.search.certificate.Certificate;
import com.olekhv.job.search.education.Education;
import com.olekhv.job.search.language.Language;
import com.olekhv.job.search.skill.Skill;
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
    @Column(name = "first_name", length = 80)
    private String firstName;
    @Column(name = "last_name", length = 80)
    private String lastName;
    @Column(name = "current_position", length = 80)
    private String currentPosition;
    @Column(name = "contact_email", length = 50)
    private String contactEmail;
    @Column(name = "phone_number", length = 20)
    private String phoneNumber;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(name = "country", length = 50)
    private String country;
    @Column(name = "city", length = 50)
    private String city;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<WorkExperience> workExperiences = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Education> educations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Certificate> certificates = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Skill> skills = new ArrayList<>();

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Language> languages = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<User> connections = new ArrayList<>();
}
