package com.olekhv.job.search.repository;

import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByApplicationsContaining(Application application);
}
