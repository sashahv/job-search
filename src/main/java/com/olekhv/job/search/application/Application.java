package com.olekhv.job.search.application;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Attachment> attachments;
    @Column(name = "status", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private ApplicationStatus status;
}
