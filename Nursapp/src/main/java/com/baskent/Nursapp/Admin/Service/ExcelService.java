package com.baskent.Nursapp.Admin.Service;

import com.baskent.Nursapp.Entity.ActionRecords.Action_Records;
import com.baskent.Nursapp.Entity.LoginRecords.Login_Records;
import com.baskent.Nursapp.Entity.Role;
import com.baskent.Nursapp.Entity.User.Users;
import com.baskent.Nursapp.Exceptions.ExcelProcessingException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelService{

    private final PasswordEncoder passwordEncoder;

    public ExcelService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public List<Users> readUsersFromExcel(MultipartFile filePath) {
        List<Users> users = new ArrayList<>();
        try (InputStream file = filePath.getInputStream();
             Workbook workbook = WorkbookFactory.create(file)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                // İlk satır genellikle başlık olduğu için atlanır
                if (row.getRowNum() == 0) continue;

                // Hücre değerlerini al ve Users nesnesini oluştur
                users.add(Users.builder()
                        .userId((long) row.getCell(0).getNumericCellValue())
                        .name(row.getCell(1).getStringCellValue())
                        .surname(row.getCell(2).getStringCellValue())
                        .kayit_tarihi(row.getCell(3).getDateCellValue()) // Excel'deki tarih bilgisini kullan
                        .password(passwordEncoder.encode(row.getCell(4).getStringCellValue()))
                        .userRole(Role.fromValue((int) row.getCell(5).getNumericCellValue()))
                        .bolumId((long) row.getCell(6).getNumericCellValue())
                        .isActive(true)
                        .build());
            }
        } catch (Exception e) {
            throw new ExcelProcessingException("Excel okuma işlemi sırasında bir hata oluştu", e);
        }
        return users;
    }



    public void exportToExcel(List<Login_Records> records, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Login Records");

        // Creating header row
        String[] headers = {"Log ID", "Action Time", "IP Address", "Requested User ID", "Requested Bolum ID", "Is Success", "Failed Reason"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Filling data
        int rowIdx = 1;
        for (Login_Records record : records) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(1).setCellValue(record.getActionTime().toString());
            row.createCell(2).setCellValue(record.getIpAddress());
            row.createCell(3).setCellValue(record.getRequestedUserId());
            row.createCell(4).setCellValue(record.getRequestedBolumId());
            row.createCell(5).setCellValue(record.getIsSuccess());
            row.createCell(6).setCellValue(record.getFailedReason());
        }

        // Auto size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to the response stream
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=login_records.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

    public void exportToExcel2(List<Action_Records> records, HttpServletResponse response) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Action Records");

        // Creating header row
        String[] headers = {"Action ID", "Action Time", "IP Address", "User ID", "Bolum ID", "Action Text"};
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Filling data
        int rowIdx = 1;
        for (Action_Records record : records) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(1).setCellValue(record.getActionTime().toString());
            row.createCell(2).setCellValue(record.getIpAddress());
            row.createCell(3).setCellValue(record.getUserid());
            row.createCell(4).setCellValue(record.getBolumId());
            row.createCell(5).setCellValue(record.getActionText());
        }

        // Auto size columns for better readability
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write the output to the response stream
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=action_records.xlsx");
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }

}
