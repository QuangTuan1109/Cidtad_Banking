package com.project.banking.service;

import com.project.banking.dto.CitadCodeDTO;

import java.io.IOException;
import java.util.List;

public interface CitadService {
    List<CitadCodeDTO> getAllCitadCode();
    CitadCodeDTO getByCitadCodeId(Long id);
    CitadCodeDTO getByCitadCode(String citadCode);
    void updateCitadCodesFromExcel() throws IOException;
}
