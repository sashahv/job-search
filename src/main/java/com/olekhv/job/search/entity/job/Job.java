package com.olekhv.job.search.entity.job;

import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.skill.Skill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "job")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "title", length = 40, nullable = false)
    private String title;
    @Column(name = "description", length = 100)
    private String description;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;
    @Column(name = "country", length = 60, nullable = false)
    private String country;
    @Column(name = "city", length = 60, nullable = false)
    private String city;
    @Column(name = "type", length = 20)
    private JobType type;
    @Column(name = "role", length = 20, nullable = false)
    private String role;
    @Column(name = "work_type", length = 10)
    @Enumerated(EnumType.STRING)
    private WorkType workType;
    @Column(name = "empty_vacancies", length = 5)
    private Integer emptyVacancies;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "job_required_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> requiredSkills = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "job_applications",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "application_id")
    )
    private List<Application> applications = new ArrayList<>();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;
}