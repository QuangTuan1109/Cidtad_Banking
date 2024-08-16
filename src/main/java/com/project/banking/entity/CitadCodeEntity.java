package com.project.banking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "citad_codes")
public class CitadCodeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "citad_code", unique = true, nullable = false)
    private String citadCode;

    private String description;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "branch_name", nullable = false)
    private String branchName;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
