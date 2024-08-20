package com.project.banking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CitadCodeDTO {
    private Long id;
    private String citadCode;
    private String bankName;
    private String branchName;
    private LocalDateTime updatedAt;
}
