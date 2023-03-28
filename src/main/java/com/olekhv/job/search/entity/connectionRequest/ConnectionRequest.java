package com.olekhv.job.search.entity.connectionRequest;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.olekhv.job.search.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.Objects;

@Entity
@Getter
@Setter
@ToString

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConnectionRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    private User fromUser;
    @JsonBackReference
    @ManyToOne(cascade = CascadeType.MERGE)
    private User toUser;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ConnectionRequest that = (ConnectionRequest) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
