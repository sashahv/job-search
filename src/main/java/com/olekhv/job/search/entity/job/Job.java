package com.olekhv.job.search.entity.job;

import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.skill.Skill;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString

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
    @Enumerated(EnumType.STRING)
    @Column(name = "type", length = 20)
    private JobType type;
    @Column(name = "role", length = 20, nullable = false)
    private String role;
    @Column(name = "work_type", length = 10)
    @Enumerated(EnumType.STRING)
    private WorkType workType;
    @Column(name = "empty_vacancies", length = 5)
    private Integer emptyVacancies;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(
            name = "job_required_skills",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "skill_id")
    )
    private List<Skill> requiredSkills = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(
            name = "job_applications",
            joinColumns = @JoinColumn(name = "job_id"),
            inverseJoinColumns = @JoinColumn(name = "application_id")
    )
    private List<Application> applications = new ArrayList<>();

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Job job = (Job) o;
        return getId() != null && Objects.equals(getId(), job.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}