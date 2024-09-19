package API.utils;

import API.pojo.register;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelFileUtils {

    private static final String[] HEADERS = {"Customer Group ID", "Store ID", "Language ID", "First Name", "Last Name",
            "Email", "Telephone", "Fax", "Password", "Salt", "Cart", "Wishlist",
            "Newsletter", "Address ID", "Custom Field", "IP", "Status", "Safe",
            "Token", "Code", "Date Added"};

    public static void writePayloadToExcel(List<register> payloads, String filePath) {
        File file = new File(filePath);
        Workbook workbook;
        Sheet sheet;
        try {
            if (file.exists()) {
                // Load existing workbook
                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fileInputStream);
                }
                sheet = workbook.getSheet("Customer Data");
                if (sheet == null) {
                    sheet = workbook.createSheet("Customer Data");
                    createHeaderRow(sheet);
                }
            } else {
                // Create new workbook and sheet if the file doesn't exist
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Customer Data");
                createHeaderRow(sheet);
            }

            int rowNum = sheet.getLastRowNum() + 1;
            for (register payload : payloads) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(payload.getCustomer_group_id());
                row.createCell(1).setCellValue(0);
                row.createCell(2).setCellValue(payload.getLanguage_id());
                row.createCell(3).setCellValue(payload.getFirstname());
                row.createCell(4).setCellValue(payload.getLastname());
                row.createCell(5).setCellValue(payload.getEmail());
                row.createCell(6).setCellValue(payload.getTelephone());
                row.createCell(7).setCellValue("");
                row.createCell(8).setCellValue(payload.getPassword());
                row.createCell(9).setCellValue("");
                row.createCell(10).setCellValue("");
                row.createCell(11).setCellValue("");
                row.createCell(12).setCellValue(payload.getNewsletter());
                row.createCell(13).setCellValue(0);
                row.createCell(14).setCellValue("");
                row.createCell(15).setCellValue("");
                row.createCell(16).setCellValue(1);
                row.createCell(17).setCellValue(0);
                row.createCell(18).setCellValue("");
                row.createCell(19).setCellValue("");
            }

            try (FileOutputStream fileOut = new FileOutputStream(new File(filePath))) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write payload to Excel file");
        }
    }

    public static void writeDeletionRecordToExcel(String email, String filePath) {
        File file = new File(filePath);
        Workbook workbook;
        Sheet sheet;
        try {
            if (file.exists()) {
                // Load existing workbook
                try (FileInputStream fileInputStream = new FileInputStream(file)) {
                    workbook = new XSSFWorkbook(fileInputStream);
                }
                sheet = workbook.getSheet("Deletion Records");
                if (sheet == null) {
                    sheet = workbook.createSheet("Deletion Records");
                    Row headerRow = sheet.createRow(0);
                    headerRow.createCell(0).setCellValue("Email");
                }
            } else {
                // Create new workbook and sheet if the file doesn't exist
                workbook = new XSSFWorkbook();
                sheet = workbook.createSheet("Deletion Records");
                Row headerRow = sheet.createRow(0);
                headerRow.createCell(0).setCellValue("Email");
            }

            // Get the last row number to append new data
            int rowNum = sheet.getLastRowNum() + 1;

            // Create data row
            Row dataRow = sheet.createRow(rowNum);
            dataRow.createCell(0).setCellValue(email);

            // Write the updated data back to the file
            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to write deletion record to Excel file");
        }
    }

    public static void removeRecordFromExcel(String email, String filePath) {
        File file = new File(filePath);
        try (FileInputStream fileInputStream = new FileInputStream(file);
             Workbook workbook = new XSSFWorkbook(fileInputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            int emailColumnIndex = findColumnIndex(sheet.getRow(0), "Email");

            for (Row row : sheet) {
                Cell cell = row.getCell(emailColumnIndex);
                if (cell != null && cell.getStringCellValue().equals(email)) {
                    sheet.removeRow(row);
                    break;
                }
            }

            try (FileOutputStream fileOut = new FileOutputStream(file)) {
                workbook.write(fileOut);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update Excel file");
        }
    }

    private static int findColumnIndex(Row headerRow, String columnName) {
        for (Cell cell : headerRow) {
            if (cell.getStringCellValue().equals(columnName)) {
                return cell.getColumnIndex();
            }
        }
        throw new RuntimeException("Column not found: " + columnName);
    }


    private static void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < HEADERS.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(HEADERS[i]);
        }
    }


}
