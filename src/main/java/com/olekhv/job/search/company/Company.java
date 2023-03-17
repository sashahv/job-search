package com.olekhv.job.search.company;

import com.olekhv.job.search.user.entity.User;
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
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> hiringTeam = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Job> jobs = new ArrayList<>();
}
