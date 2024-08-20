package com.project.banking.util;

import com.project.banking.dto.CitadCodeDTO;
import com.project.banking.entity.CitadCodeEntity;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelReader {
    public List<CitadCodeEntity> readCitadCodeFromExcel(String filePath) throws IOException {
        List<CitadCodeEntity> citadCodeEntityList = new ArrayList<>();

        FileInputStream fileInputStream = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            CitadCodeEntity citadCodeEntity = new CitadCodeEntity();
            citadCodeEntity.setId(getLongCellValue(row.getCell(0)));
            citadCodeEntity.setCitadCode(getCellValue(row.getCell(1)));
            citadCodeEntity.setBankName(getCellValue(row.getCell(2)));
            citadCodeEntity.setBranchName(getCellValue(row.getCell(3)));
            citadCodeEntityList.add(citadCodeEntity);
        }

        workbook.close();
        fileInputStream.close();

        return citadCodeEntityList;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.format("%.0f", cell.getNumericCellValue());
            default:
                return "";
        }
    }

    private Long getLongCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        switch (cell.getCellType()) {
            case NUMERIC:
                return (long) cell.getNumericCellValue();
            case STRING:
                try {
                    return Long.parseLong(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return null;
                }
            default:
                return null;
        }
    }
}
