package com.olekhv.job.search.repository;

import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.job.Job;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Optional<Job> findByApplicationsContaining(Application application);

    @Query("SELECT j " +
            "FROM Job j " +
            "WHERE CONCAT(j.title, ' ', j.description, ' ', j.role) LIKE %?1%")
    List<Job> findAll(String keyword);

    List<Job> findAll(Specification<Job> specification);
}
