package org.example;

import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ReadExcel {

    public static void main(String[] args) {
        // Configure these variables
        String filePath =  "Book1.xlsx";
        int sheetIndex = 0; // Specify the sheet index to read (0-based)
        String columnsToReadString = "-1"; // Example: Reading columns by names

        // Determine if all columns should be read
        boolean readAllColumns = columnsToReadString.contains("-1");

        // Parse the columns to read string into a set of column names
        Set<String> columnsToRead = new HashSet<>();
        if (!readAllColumns) {
            String[] columnsArray = columnsToReadString.split(",");
            for (String col : columnsArray) {
                columnsToRead.add(col.trim());
            }
        }

        try (
                FileInputStream fis = new FileInputStream(filePath);
                Workbook workbook = new XSSFWorkbook(fis)
        ) {
            // Validate the sheet index
            int numberOfSheets = workbook.getNumberOfSheets();
            if (sheetIndex < 0 || sheetIndex >= numberOfSheets) {
                System.out.println("Invalid sheet index.");
                return;
            }

            Sheet sheet = workbook.getSheetAt(sheetIndex);
            System.out.println("Reading sheet: " + sheet.getSheetName());

            // Read the headers to map column names to indices
            Row headerRow = sheet.getRow(0);
            Map<String, Integer> columnIndices = new HashMap<>();
            if (headerRow != null) {
                for (Cell cell : headerRow) {
                    columnIndices.put(cell.getStringCellValue(), cell.getColumnIndex());
                }
            }

            for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row != null) {
                    for (String columnName : columnIndices.keySet()) {
                        if (readAllColumns || columnsToRead.contains(columnName)) {
                            int cellIndex = columnIndices.get(columnName);
                            Cell cell = row.getCell(
                                    cellIndex,
                                    Row.MissingCellPolicy.CREATE_NULL_AS_BLANK
                            );
                            String cellValue = getCellValueAsString(cell);
                            System.out.println(columnName + ": " + cellValue);
                        }
                    }
                    System.out.println();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to get cell value as String
    private static String getCellValueAsString(Cell cell) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return dateFormat.format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case STRING:
                        return cell.getStringCellValue();
                    case NUMERIC:
                        return String.valueOf(cell.getNumericCellValue());
                    case BOOLEAN:
                        return String.valueOf(cell.getBooleanCellValue());
                    default:
                        return "UNKNOWN";
                }
            case BLANK:
                return "";
            default:
                return "UNKNOWN";
        }
    }
}
