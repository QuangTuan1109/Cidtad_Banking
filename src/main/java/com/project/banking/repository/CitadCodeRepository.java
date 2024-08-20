package com.project.banking.repository;

import com.project.banking.dto.CitadCodeDTO;
import com.project.banking.entity.CitadCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CitadCodeRepository extends JpaRepository<CitadCodeEntity, Long> {
    Optional<CitadCodeEntity> findByCitadCode(String citadCode);
}
