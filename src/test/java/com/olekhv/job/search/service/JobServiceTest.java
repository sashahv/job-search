package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.dataobjects.JobDO;
import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.education.Education;
import com.olekhv.job.search.entity.job.Job;
import com.olekhv.job.search.entity.user.User;
import com.olekhv.job.search.exception.NoPermissionException;
import com.olekhv.job.search.repository.CompanyRepository;
import com.olekhv.job.search.repository.JobRepository;
import com.olekhv.job.search.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JobServiceTest {
    @InjectMocks private JobService jobService;

    @Mock private User user;
    @Mock private UserCredential userCredential;
    @Mock private JobDO jobDO;
    @Mock private Job job;
    @Mock private JobRepository jobRepository;
    @Mock private Company company;
    @Mock private CompanyRepository companyRepository;
    @Mock private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        when(userCredential.getUser()).thenReturn(user);
        when(company.getOwner()).thenReturn(user);
        when(companyRepository.findById(any(Long.class))).thenReturn(Optional.of(company));
        when(jobRepository.findById(any(Long.class))).thenReturn(Optional.of(job));
    }

    @Test
    void should_create_new_job(){
        // Given
        when(company.getJobs()).thenReturn(new ArrayList<>());

        // When
        jobService.createNewJob(jobDO, 1L, userCredential);

        // Then
        verify(jobRepository,times(1)).save(any(Job.class));
        verify(companyRepository,times(1)).save(any(Company.class));
        assertEquals(1, company.getJobs().size());
    }

    // If user does not have next permissions:
    //         * he's recruiter
    //         * owns company
    //         * heads company
    // he should not be able to create new job
    @Test
    void should_throw_exception_if_no_permission(){
        assertThrows(NoPermissionException.class, () ->
                jobService.createNewJob(jobDO, 1L, new UserCredential()));
    }

    @Test
    void should_make_job_inactive_if_expired(){
        List<Job> expiredJobs = List.of(
                Job.builder().isActive(true).expiresAt(LocalDateTime.now().minusDays(1)).build(),
                Job.builder().isActive(true).expiresAt(LocalDateTime.now().plusDays(1)).build(),
                Job.builder().isActive(false).expiresAt(LocalDateTime.now().plusDays(1)).build()
        );
        when(jobRepository.findAll()).thenReturn(expiredJobs);

        jobService.makeExpiredJobsInactive();

        assertFalse(expiredJobs.get(0).getIsActive());
        assertTrue(expiredJobs.get(1).getIsActive());
        assertFalse(expiredJobs.get(2).getIsActive());
        verify(jobRepository, times(1)).save(any(Job.class));
    }

    @Test
    void should_save_job(){
        // Given
        when(user.getSavedJobs()).thenReturn(new ArrayList<>());

        // When
        jobService.saveJob(1L, userCredential);

        // Then
        verify(userRepository,times(1)).save(user);
        assertEquals(1, user.getSavedJobs().size());
    }

    @Test
    void should_delete_saved_job(){
        // Given
        when(user.getSavedJobs()).thenReturn(new ArrayList<>(Collections.singletonList(job)));

        // When
        jobService.deleteSavedJob(1L, userCredential);

        // Then
        verify(userRepository,times(1)).save(user);
        assertEquals(0, user.getSavedJobs().size());
    }
}