package com.olekhv.job.search.entity.application;

import com.olekhv.job.search.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "file_type", length = 30, nullable = false)
    private String fileType;
    @Lob
    @Column(name = "data", length = 3145728, nullable = false)
    private byte[] data; // max size - 3 MB
    @OneToOne
    private User owner;
}