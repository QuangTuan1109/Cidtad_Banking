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

        List<CitadCodeEntity> existingCitadCodes = citadCodeRepository.findAll();

        detectAndLogChanges(newCitadCodes, existingCitadCodes, citadCodesInExcel);

        // Update or add entries
        for (CitadCodeDTO citadCodeDTO : newCitadCodes) {
            if (isValid(citadCodeDTO)) {
                CitadCodeEntity citadCodeEntity = CitadCodeMapper.mapToCitadCode(citadCodeDTO);

                citadCodeRepository.findByCitadCode(citadCodeEntity.getCitadCode())
                        .ifPresentOrElse(existingCode -> {
                            existingCode.setBankName(citadCodeEntity.getBankName());
                            existingCode.setBranchName(citadCodeEntity.getBranchName());
                            citadCodeRepository.save(existingCode);
                        }, () -> citadCodeRepository.save(citadCodeEntity));
            } else {
                logger.error("Invalid data: " + citadCodeDTO);
            }
        }

        // Remove entries not present in the Excel file
        for (CitadCodeEntity citadCodeEntity : existingCitadCodes) {
            if (!citadCodesInExcel.contains(citadCodeEntity.getCitadCode())) {
                citadCodeRepository.delete(citadCodeEntity);
            }
        }
    }

    private void detectAndLogChanges(List<CitadCodeDTO> newCitadCodes, List<CitadCodeEntity> existingCitadCodes, List<String> citadCodesInExcel) {

        for (CitadCodeDTO citadCodeDTO : newCitadCodes) {
            CitadCodeEntity existingEntity = existingCitadCodes.stream()
                    .filter(e -> e.getCitadCode().equals(citadCodeDTO.getCitadCode()))
                    .findFirst()
                    .orElse(null);

            if (existingEntity == null) {
                logger.warn("New CitadCode detected: " + citadCodeDTO.getCitadCode());
            } else if (!existingEntity.getBankName().equals(citadCodeDTO.getBankName()) ||
                    !existingEntity.getBranchName().equals(citadCodeDTO.getBranchName())) {
                logger.warn("CitadCode updated: " + citadCodeDTO.getCitadCode());
            }
        }

        for (CitadCodeEntity existingEntity : existingCitadCodes) {
            if (!citadCodesInExcel.contains(existingEntity.getCitadCode())) {
                logger.warn("CitadCode deleted: " + existingEntity.getCitadCode());
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
