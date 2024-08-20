package com.project.banking.mapper;

import com.project.banking.dto.CitadCodeDTO;
import com.project.banking.entity.CitadCodeEntity;

public class CitadCodeMapper {
    public static CitadCodeEntity mapToCitadCode(CitadCodeDTO citadCodeDTO) {
        CitadCodeEntity citadCodeEntity = new CitadCodeEntity(
                citadCodeDTO.getId(),
                citadCodeDTO.getCitadCode(),
                citadCodeDTO.getBankName(),
                citadCodeDTO.getBranchName(),
                citadCodeDTO.getUpdatedAt()
        );

        return citadCodeEntity;
    }

    public static CitadCodeDTO mapToCitadCodeDto(CitadCodeEntity citadCodeEntity) {
        CitadCodeDTO citadCodeDTO = new CitadCodeDTO(
                citadCodeEntity.getId(),
                citadCodeEntity.getCitadCode(),
                citadCodeEntity.getBankName(),
                citadCodeEntity.getBranchName(),
                citadCodeEntity.getUpdatedAt()
        );

        return citadCodeDTO;
    }
}
