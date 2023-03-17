package com.olekhv.job.search.application;

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
    @Column(name = "description", length = 50, nullable = false)
    private String description;
    @Column(name = "category", length = 20, nullable = false)
    private AttachmentCategory category;
    @Column(name = "file_type", length = 30, nullable = false)
    private String fileType;
    @Column(name = "data", nullable = false)
    private byte[] data;
}