package com.olekhv.job.search.repository;

import com.olekhv.job.search.entity.company.Company;
import com.olekhv.job.search.entity.job.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);

    Optional<Company> findByJobsIsContaining(Job job);
}
