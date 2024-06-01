package com.baskent.Nursapp.Student.Controller;

import com.baskent.Nursapp.Admin.DTO.AllUsersResponseUser;
import com.baskent.Nursapp.Auth.Models.AuthenticationResponse;
import com.baskent.Nursapp.Auth.Models.ChangePassword;
import com.baskent.Nursapp.Auth.Service.AuthenticationService;
import com.baskent.Nursapp.DataAccessLayer.BolumRepository;

import com.baskent.Nursapp.Entity.Subskills;
import com.baskent.Nursapp.Student.Service.StudentService;
import com.baskent.Nursapp.Teacher.DTO.*;

import com.baskent.Nursapp.Teacher.Service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/nursapp/app/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final AuthenticationService service;

    @PostMapping("/changepassword")
    public ResponseEntity<AuthenticationResponse> ChangePassword(@RequestBody ChangePassword changePassword){
        return ResponseEntity.ok(service.changepassword(changePassword));
    }

    @GetMapping("/getskills/{bolumid}/{yilsayisi}/{secondgraderid}")
    public SkillsWithSecondGraderInfo getSkills(@PathVariable("bolumid") Long bolumid, @PathVariable("yilsayisi") Long yilsayisi, @PathVariable("secondgraderid") Long secondgraderid){
        return studentService.getSkillsByBolumId(bolumid, yilsayisi, secondgraderid);
    }
    @GetMapping("/getsubskillsforstatistics/{studentid}/{bolumid}/{classlevel}/{skillid}/{baserole}")
    public SubskillsForStatistics getSubSkillsForStatistics(@PathVariable("studentid") Integer studentid, @PathVariable("bolumid") Integer bolumid, @PathVariable("classlevel") Integer classlevel, @PathVariable("skillid") Integer skillid, @PathVariable("baserole") Integer baserole){

        return studentService.getSubSkillsForStatistics(studentid, bolumid, classlevel, skillid, baserole);
    }
    @GetMapping("/getsubskills/{studentid}/{bolumid}/{classlevel}/{skillid}")
    public List<SubSkillwithGrades> getSubSkills(@PathVariable("studentid") Integer studentid, @PathVariable("bolumid") Integer bolumid, @PathVariable("classlevel") Integer classlevel, @PathVariable("skillid") Integer skillid){
        return studentService.getSubSkillsBySkillid(studentid, bolumid, classlevel, skillid);
    }

    @GetMapping("/getsubskillstepsforstatistics/{subskillid}/{studentid}/{bolumid}/{classlevel}/{baserole}")
    public SubskillStepsForStatistics getSubSkillStepsBySubSkillid(@PathVariable("subskillid") Long subskillid, @PathVariable("studentid") Long studentid, @PathVariable("bolumid") Long bolumid, @PathVariable("classlevel") Long classlevel, @PathVariable("baserole") Integer baserole){
        return studentService.getSubSkillStepsForStatistics(subskillid, studentid, bolumid,classlevel, baserole);
    }

    @GetMapping("/getgrader/{graderid}/{bolumid}/{graderrole}")
    public GraderInfo getGrader(@PathVariable("graderid") Long graderid,@PathVariable("bolumid") Long bolumid,@PathVariable("graderrole") Long graderrole){
        return studentService.getGraderInfo(graderid  , bolumid, graderrole);
    }

    @GetMapping("/getsubskillsteps/{subskillid}/{studentid}/{bolumid}/{classlevel}")
    public SubskillStepsAndEquipments getSubSkillSteps(@PathVariable("subskillid") Long subskillid, @PathVariable("studentid") Long studentid, @PathVariable("bolumid") Long bolumid, @PathVariable("classlevel") Long classlevel){
        return studentService.getSubSkillStepsBySubSkillid(subskillid, studentid, bolumid,classlevel);
    }

    @GetMapping("/getstatistics/{studentid}/{bolumid}/{yilsayisi}")
    public StatisticsWithAverage getStatistics(@PathVariable("studentid") Long studentid, @PathVariable("bolumid") Long bolumid, @PathVariable("yilsayisi") Long yilsayisi){
        return studentService.getStatistics( studentid, bolumid, yilsayisi );
    }

    @PostMapping("/savegrades")
    public void saveGrades(@RequestBody SavePerforms saveGrades){
        studentService.saveGrades(saveGrades);
    }

}

