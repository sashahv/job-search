package com.olekhv.job.search.company;

import com.olekhv.job.search.application.Application;
import com.olekhv.job.search.user.entity.Skill;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
    @Column(name = "country", nullable = false)
    private String country;
    @Column(name = "city", nullable = false)
    private String city;
    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private JobType type;
    @Column(name = "experience", nullable = false)
    private String experience;
    @Column(name = "work_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WorkType workType;
    @Column(name = "empty_vacancies", nullable = false)
    private Integer emptyVacancies;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Application> applications;
}