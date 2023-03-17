package com.olekhv.job.search.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "certificate")
public class Certificate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", length = 200, nullable = false)
    private String name;
    @Column(name = "organization", length = 100, nullable = false)
    private String organization;
    @Column(name = "issue_date")
    private LocalDate issueDate;
    @Column(name = "expiration_date")
    private LocalDate expirationDate;
    @Column(name = "url")
    private String url;
}
