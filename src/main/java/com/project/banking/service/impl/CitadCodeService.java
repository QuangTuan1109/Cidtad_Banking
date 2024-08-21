package com.project.banking.service.impl;

import com.project.banking.dto.CitadCodeDTO;
import com.project.banking.entity.CitadCodeEntity;
import com.project.banking.mapper.CitadCodeMapper;
import com.project.banking.repository.CitadCodeRepository;
import com.project.banking.service.CitadService;
import com.project.banking.util.ExcelReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitadCodeService implements CitadService {

    private final CitadCodeRepository citadCodeRepository;
    private final ExcelReader excelReader;
    private static final Logger logger = LoggerFactory.getLogger(CitadCodeService.class);

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
    public void updateCitadCodesFromExcel() throws IOException {
        String excelFilePath = "C:\\Users\\ADMIN\\Downloads\\personal-internet-local-clearing-code.xlsx";
        List<CitadCodeDTO> newCitadCodes = excelReader.readCitadCodeDTOFromExcel(excelFilePath);

        List<String> citadCodesInExcel = newCitadCodes.stream()
                .map(CitadCodeDTO::getCitadCode)
                .collect(Collectors.toList());

        // Update or add entries
        for (CitadCodeDTO citadCodeDTO : newCitadCodes) {
            if (isValid(citadCodeDTO)) {
                CitadCodeEntity citadCodeEntity = CitadCodeMapper.mapToCitadCode(citadCodeDTO);

                citadCodeRepository.findByCitadCode(citadCodeEntity.getCitadCode())
                        .ifPresentOrElse(existingCode -> {
                            existingCode.setBankName(citadCodeEntity.getBankName());
                            existingCode.setBranchName(citadCodeEntity.getBranchName());
                            citadCodeRepository.save(existingCode);
                        }, () -> {
                            citadCodeRepository.save(citadCodeEntity);
                            logger.info("Added new CitadCode: " + citadCodeEntity.getCitadCode());
                        });
            } else {
                logger.error("Invalid data: " + citadCodeDTO);
            }
        }

        // Remove entries not present in the Excel file
        List<CitadCodeEntity> existingCitadCodes = citadCodeRepository.findAll();
        for (CitadCodeEntity citadCodeEntity : existingCitadCodes) {
            if (!citadCodesInExcel.contains(citadCodeEntity.getCitadCode())) {
                citadCodeRepository.delete(citadCodeEntity);
                logger.info("Deleted from DB: " + citadCodeEntity.getCitadCode());
            }
        }
    }


    private boolean isValid(CitadCodeDTO dto) {
        if (dto.getId() == null || dto.getCitadCode().isEmpty() || dto.getBankName().isEmpty() || dto.getBranchName().isEmpty()) {
            return false;
        }
        return true;
    }
}
