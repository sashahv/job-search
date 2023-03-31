package com.olekhv.job.search.entity.company;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "company_heads",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_head_id")
    )
    private List<User> heads = new ArrayList<>();

    @OneToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(
            name = "company_hiring_team",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "user_recruiter_id")
    )
    private List<User> hiringTeam = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinTable(
            name = "company_jobs",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private List<Job> jobs = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Company company = (Company) o;
        return getId() != null && Objects.equals(getId(), company.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
