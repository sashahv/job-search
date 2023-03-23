package com.olekhv.job.search.entity.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.olekhv.job.search.entity.certificate.Certificate;
import com.olekhv.job.search.entity.education.Education;
import com.olekhv.job.search.entity.language.UserLanguage;
import com.olekhv.job.search.entity.skill.Skill;
import com.olekhv.job.search.entity.connectionRequest.ConnectionRequest;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
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
    private List<UserLanguage> languages = new ArrayList<>();

    @JsonBackReference
    @OneToMany(cascade = {CascadeType.DETACH, CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<User> connections = new ArrayList<>();

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", currentPosition='" + currentPosition + '\'' +
                ", contactEmail='" + contactEmail + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthDate=" + birthDate +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", workExperiences=" + workExperiences +
                ", educations=" + educations +
                ", certificates=" + certificates +
                ", skills=" + skills +
                ", languages=" + languages +
                '}';
    }
}
