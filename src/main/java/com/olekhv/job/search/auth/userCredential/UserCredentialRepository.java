package com.olekhv.job.search.auth.userCredential;

import com.olekhv.job.search.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserCredentialRepository extends JpaRepository<UserCredential, Long> {
    Optional<UserCredential> findByEmail(String email);

    Optional<UserCredential> findByUser(User user);
}
