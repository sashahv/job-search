package com.olekhv.job.search.entity.connectionRequest;

import com.olekhv.job.search.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRequestRepository extends JpaRepository<ConnectionRequest, Long> {
    Optional<List<ConnectionRequest>> findByToUser(User user);
    Optional<ConnectionRequest> findByFromUserAndToUser(User fromUser, User toUser);
}
