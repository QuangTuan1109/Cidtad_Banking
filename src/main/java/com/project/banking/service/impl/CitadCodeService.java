package com.project.banking.service.impl;

import com.project.banking.dto.CitadCodeDTO;
import com.project.banking.entity.CitadCodeEntity;
import com.project.banking.mapper.CitadCodeMapper;
import com.project.banking.repository.CitadCodeRepository;
import com.project.banking.service.CitadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitadCodeService implements CitadService {

    private CitadCodeRepository citadCodeRepository;

    public CitadCodeService(CitadCodeRepository citadCodeRepository) {
        this.citadCodeRepository = citadCodeRepository;
    }

    @Override
    public List<CitadCodeDTO> getAllCitadCode() {
        List<CitadCodeEntity> citadCodeEntities = citadCodeRepository.findAll();
        return citadCodeEntities.stream()
                .map(CitadCodeMapper::mapToCitadCodeDto)
                .collect(Collectors.toList());
    }

    @Override
    public CitadCodeDTO getByCitadCodeId(Long id) {
        CitadCodeEntity citadCodeEntity = citadCodeRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Not Exist"));
        return CitadCodeMapper.mapToCitadCodeDto(citadCodeEntity);
    }

    @Override
    public CitadCodeDTO getByCitadCode(String citadCode) {
        CitadCodeEntity citadCodeEntity = citadCodeRepository.findByCitadCode(citadCode)
                .orElseThrow(() -> new RuntimeException("Citad code not found"));
        return CitadCodeMapper.mapToCitadCodeDto(citadCodeEntity);
    }
}
