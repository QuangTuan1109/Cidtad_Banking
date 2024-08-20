package com.project.banking.job;

import com.project.banking.service.impl.CitadCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CitadCodeSyncJob {
    private static final Logger LOGGER = LoggerFactory.getLogger(CitadCodeSyncJob.class);

    private final CitadCodeService citadCodeService;

    public CitadCodeSyncJob (CitadCodeService citadCodeService) {
        this.citadCodeService = citadCodeService;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void syncCitadCodeFromExcel() {
        citadCodeService.updateCitadCodesFromExcel();
    }
}
