package com.project.banking.controller;

import com.project.banking.dto.CitadCodeDTO;
import com.project.banking.service.impl.CitadCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/citad")
public class CitadCodeController {
    private CitadCodeService citadCodeService;

    public CitadCodeController(CitadCodeService citadCodeService) {
        this.citadCodeService = citadCodeService;
    }
    @GetMapping
    public ResponseEntity<List<CitadCodeDTO>> getAllCitadCodes() {
        List<CitadCodeDTO> citadCodeDTOs = citadCodeService.getAllCitadCode();
        return ResponseEntity.ok(citadCodeDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CitadCodeDTO> getCitadCodeById(@PathVariable Long id) {
        CitadCodeDTO citadCodeDTO = citadCodeService.getByCitadCodeId(id);
        return ResponseEntity.ok(citadCodeDTO);
    }

    @GetMapping("/code/{citadCode}")
    public ResponseEntity<CitadCodeDTO> getCitadCode(@PathVariable String citadCode) {
        CitadCodeDTO citadCodeDTO = citadCodeService.getByCitadCode(citadCode);
        return ResponseEntity.ok(citadCodeDTO);
    }
}
