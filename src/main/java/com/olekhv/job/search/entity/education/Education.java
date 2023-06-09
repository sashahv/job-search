package com.olekhv.job.search.entity.education;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString

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
    @Column(name = "from_date")
    private LocalDate fromDate;
    @Column(name = "to_date")
    private LocalDate toDate;
    @Column(name = "avg_grade", length = 10)
    private Double averageGrade;
    @Column(name = "additional_info", length = 200)
    private String additionalInformation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Education education = (Education) o;
        return getId() != null && Objects.equals(getId(), education.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
