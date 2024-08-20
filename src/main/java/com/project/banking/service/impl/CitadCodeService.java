package com.project.banking.service.impl;

import com.project.banking.dto.CitadCodeDTO;
import com.project.banking.entity.CitadCodeEntity;
import com.project.banking.mapper.CitadCodeMapper;
import com.project.banking.repository.CitadCodeRepository;
import com.project.banking.service.CitadService;
import com.project.banking.util.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitadCodeService implements CitadService {

    private final CitadCodeRepository citadCodeRepository;
    private final ExcelReader excelReader;

    public CitadCodeService(CitadCodeRepository citadCodeRepository, ExcelReader excelReader) {
        this.citadCodeRepository = citadCodeRepository;
        this.excelReader = excelReader;
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

    @Override
    public void updateCitadCodesFromExcel() {
        String excelFilePath = "C:\\Users\\ADMIN\\Downloads\\personal-internet-local-clearing-code.xlsx";

        try {
            List<CitadCodeEntity> newCitadCodes = excelReader.readCitadCodeFromExcel(excelFilePath);

            for (CitadCodeEntity citadCodeEntity : newCitadCodes) {
                citadCodeRepository.findByCitadCode(citadCodeEntity.getCitadCode())
                        .ifPresentOrElse(existingCode -> {
                            existingCode.setBankName(citadCodeEntity.getBankName());
                            existingCode.setBranchName(citadCodeEntity.getBranchName());
                            citadCodeRepository.save(existingCode);
                        }, () -> citadCodeRepository.save(citadCodeEntity));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
