package com.olekhv.job.search.entity.company;

import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "tax_id", nullable = false)
    private String taxId;
    @Column(name = "name", length = 60, nullable = false)
    private String name;
    @Column(name = "address", length = 70, nullable = false)
    private String address;
    @Column(name = "email", length = 50, nullable = false)
    private String email;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private User owner;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(
            name = "company_heads",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_head_id")
    )
    private List<User> heads = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH}, orphanRemoval = true)
    @JoinTable(
            name = "company_hiring_team",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_recruiter_id")
    )
    private List<User> hiringTeam = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinTable(
            name = "company_jobs",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private List<Job> jobs = new ArrayList<>();
}
