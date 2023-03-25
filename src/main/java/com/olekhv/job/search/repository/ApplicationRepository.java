package com.olekhv.job.search.repository;

import com.olekhv.job.search.entity.application.Application;
import com.olekhv.job.search.entity.application.ApplicationStatus;
import com.olekhv.job.search.entity.application.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findByStatusAndCreatedAtBefore(ApplicationStatus applicationStatus, LocalDateTime createdAt);
    List<Application> deleteAllByStatusAndCreatedAtBefore(ApplicationStatus[] applicationStatuses, LocalDateTime createdAt);
    Optional<Application> findByAttachmentsContaining(Attachment attachment);
}
