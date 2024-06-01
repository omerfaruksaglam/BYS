package com.baskent.Nursapp.Admin.Controller;

import com.baskent.Nursapp.Admin.Service.AdminService;
import com.baskent.Nursapp.Admin.Service.ExcelService;
import com.baskent.Nursapp.DataAccessLayer.Action_RecordRepository;
import com.baskent.Nursapp.DataAccessLayer.Login_RecordsRepository;
import com.baskent.Nursapp.Entity.ActionRecords.Action_Records;
import com.baskent.Nursapp.Entity.LoginRecords.Login_Records;
import com.baskent.Nursapp.Entity.User.Users;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.util.List;
@RestController
@RequestMapping("/nursapp/app/admin")
@RequiredArgsConstructor
public class ExcelController {

    private final ExcelService excelService;
    private final AdminService adminService;
    private final Login_RecordsRepository repository;
    private final Action_RecordRepository actionRecordRepository;
    @PostMapping("/excel-upload/{admin}")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile filePath, @PathVariable("admin") Long admin) {
        try {
            List<Users> users = excelService.readUsersFromExcel(filePath);
            adminService.saveUsers(users, admin);
            return ResponseEntity.ok("Excel yükleme işlemi başarılı");
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST) // veya HttpStatus.INTERNAL_SERVER_ERROR
                    .body("Excel yükleme işlemi sırasında bir hata oluştu: " + e.getMessage());
        }
    }


    @GetMapping("/exportloginrecords/{bolumid}")
    public void exportToExcel(HttpServletResponse response, @PathVariable("bolumid") Long bolumid) throws IOException {
        List<Login_Records> records = repository.getLogin_RecordsByRequestedBolumId(bolumid);
        excelService.exportToExcel(records, response);
    }

    @GetMapping("/exportactionrecords/{bolumid}")
    public void exportToExcel2(HttpServletResponse response, @PathVariable("bolumid") Long bolumid) throws IOException {
        List<Action_Records> records = actionRecordRepository.getAllByBolumId(bolumid);
        excelService.exportToExcel2(records, response);
    }
}

