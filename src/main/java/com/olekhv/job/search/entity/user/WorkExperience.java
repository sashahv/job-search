package com.olekhv.job.search.entity.user;

import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.job.WorkType;
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
public class WorkExperience {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @JoinColumn(name = "company_id", nullable = false)
    @OneToOne
    private Company company;
    @Column(name = "industry", length = 100)
    private String industry;
    @Column(name = "work_type", length = 100)
    @Enumerated(EnumType.STRING)
    private WorkType workType;
    @Column(name = "from_date", length = 100)
    private LocalDate fromDate;
    @Column(name = "to_date", length = 100)
    private LocalDate toDate;
    @Column(name = "description", length = 30)
    private String description;
}
