package com.baskent.Nursapp.Admin.Controller;

import com.baskent.Nursapp.Admin.DTO.PasswordChangeDTO;
import com.baskent.Nursapp.Admin.DTO.UserUpdateDTO;
import com.baskent.Nursapp.Auth.Models.AuthenticationResponse;
import com.baskent.Nursapp.Auth.Models.RegisterRequest;
import com.baskent.Nursapp.Auth.Service.AuthenticationService;
import com.baskent.Nursapp.Admin.DTO.AllUsers;
import com.baskent.Nursapp.Admin.Service.AdminService;
import com.baskent.Nursapp.Admin.DTO.AvgWithYear;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nursapp/app/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @PutMapping("/changepassword")
    public ResponseEntity ChangePassword(@RequestBody PasswordChangeDTO passwordChangeDTO){
        return ResponseEntity.ok(service.changepassword(passwordChangeDTO));
    }

    @GetMapping("/getusers/{bolumid}")
    public AllUsers getUsers(@PathVariable("bolumid") Long bolumid){
        return adminService.getUsersByBolumId(bolumid);
    }

    @PutMapping("/updateuser/{bolumid}-{userid}-{admin}")
    public ResponseEntity<?> updateUser(@PathVariable("bolumid") Long bolumid, @PathVariable("userid") Long userid, @PathVariable("admin") Long admin , @RequestBody UserUpdateDTO userDetails){
        try {
            adminService.updateUser(userid, bolumid, userDetails, admin);
            return ResponseEntity.ok().body("Kullanıcı güncelleme işlemi başarılı");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Kullanıcı güncelleme işlemi başarısız: " + e.getMessage());
        }
    }

    @PutMapping("/isactive/{bolumid}-{userid}-{admin}")
    public ResponseEntity<?> isActive(@PathVariable("bolumid") Long bolumid, @PathVariable("userid") Long userid, @PathVariable Long admin, @RequestParam("isActive") boolean isActive){
        try {
            adminService.updateActiveStatus(userid, bolumid, isActive, admin);
            return ResponseEntity.ok().body("Kullanıcı aktiflik durumu güncelleme işlemi başarılı");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Kullanıcı aktiflik durumu güncelleme işlemi başarısız: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteuser/{bolumid}-{userid}-{admin}")
    public ResponseEntity<String> deleteUser(@PathVariable("bolumid") Long bolumid, @PathVariable("userid") Long userid, @PathVariable Long admin){
        try {
            adminService.deleteUser(userid, bolumid, admin);
            return ResponseEntity.ok().body("Kullanıcı silme işlemi başarılı");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Kullanıcı silme işlemi başarısız: " + e.getMessage());
        }
        
    }

    @GetMapping("/getloginrecords/{bolumid}")
    public ResponseEntity<?> getLoginRecords(@PathVariable("bolumid") Long bolumid){
        return ResponseEntity.ok(adminService.getloginActivities(bolumid));
    }

    @GetMapping("/getactionrecords/{bolumid}")
    public ResponseEntity<?> getActionRecords(@PathVariable("bolumid") Long bolumid){
        return ResponseEntity.ok(adminService.getActionRecords(bolumid));
    }

    @GetMapping("/getavgwithyears/{skillid}")
    public List<AvgWithYear> getAvgWithYears(@PathVariable("skillid") Integer skillid){
        return adminService.getAvgWithYears(skillid);
    }
}
