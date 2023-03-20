package com.olekhv.job.search.entity.language;

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
@Table(name = "user_language")
public class UserLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    private Language language;
    @Enumerated(EnumType.STRING)
    private LanguageProficiency proficiency;
}
