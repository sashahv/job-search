package com.olekhv.job.search.repository;

import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.skill.Skill;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestEntityManager()
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JobRepositoryTest {
    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private JobRepository jobRepository;

    @Mock
    private CompanyRepository companyRepository;

    @Test
    public void testCannotDeleteJobWithApplications() {
        // Create a job entity and save it to the database
        Job job = Job.builder()
                .title("Test Job")
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(30))
                .country("USA")
                .city("New York")
                .role("Developer")
                .emptyVacancies(1)
                .applications(Collections.singletonList(Application.builder().build()))
                .isActive(true)
                .build();
        jobRepository.save(job);

        // Attempt to delete the job entity
        assertThrows(org.springframework.dao.DataIntegrityViolationException.class, () -> {
            jobRepository.delete(job);
        });
    }
}