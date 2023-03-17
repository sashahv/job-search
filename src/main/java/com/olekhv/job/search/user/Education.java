package com.olekhv.job.search.user;

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
@Table(name = "education")
public class Education {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "school", length = 100, nullable = false)
    private String school;
    @Column(name = "degree", length = 50, nullable = false)
    private String degree;
    @Column(name = "field_of_study", length = 100, nullable = false)
    private String fieldOfStudy;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "graduation_date")
    private LocalDate graduationDate;
    @Column(name = "avg_grade", length = 10)
    private Double averageGrade;
    @Column(name = "additional_info", length = 200)
    private String additionalInformation;
}
