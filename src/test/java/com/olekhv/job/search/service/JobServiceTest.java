package com.olekhv.job.search.service;

import com.olekhv.job.search.auth.userCredential.UserCredential;
import com.olekhv.job.search.dataobjects.JobDO;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class JobServiceTest {
    @InjectMocks private JobService jobService;

    @Mock private User user;
    @Mock private UserCredential userCredential;
    @Mock private JobDO jobDO;
    @Mock private JobRepository jobRepository;
    @Mock private Company company;
    @Mock private CompanyRepository companyRepository;

    @BeforeEach
    void setUp() {
        when(userCredential.getUser()).thenReturn(user);
        when(company.getOwner()).thenReturn(user);
        when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
    }

    @Test
    void should_create_new_job(){
        when(company.getJobs()).thenReturn(new ArrayList<>());

        jobService.createNewJob(jobDO, 1L, userCredential);

        verify(jobRepository,times(1)).save(any(Job.class));
        verify(companyRepository,times(1)).save(any(Company.class));
        assertThat(company.getJobs().size()).isEqualTo(1);
    }

    @Test
    void should_throw_exception_if_no_permission(){
        assertThrows(NoPermissionException.class, () ->
                jobService.createNewJob(jobDO, 1L, new UserCredential()));
    }
}