package com.baskent.Nursapp.Teacher.Controller;
import com.baskent.Nursapp.Admin.DTO.AllUsersResponseUser;
import com.baskent.Nursapp.Auth.Models.AuthenticationResponse;
import com.baskent.Nursapp.Auth.Models.ChangePassword;
import com.baskent.Nursapp.Auth.Service.AuthenticationService;
import com.baskent.Nursapp.Teacher.DTO.*;
import com.baskent.Nursapp.Teacher.Service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/nursapp/app/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final TeacherService teacherService;
    private final AuthenticationService service;

    @PostMapping("/changepassword")
    public ResponseEntity<AuthenticationResponse> ChangePassword(@RequestBody ChangePassword changePassword){
        return ResponseEntity.ok(service.changepassword(changePassword));
    }


    @GetMapping("/getusers/{bolumid}")
    public List<AllUsersResponseUser> getUsers(@PathVariable("bolumid") Long bolumid){
        return teacherService.getUsersByBolumIdandRol(bolumid);
    }

    @GetMapping("/getgrader/{graderid}/{bolumid}/{graderrole}")
    public GraderInfo getGrader(@PathVariable("graderid") Long graderid,@PathVariable("bolumid") Long bolumid,@PathVariable("graderrole") Long graderrole){
        return teacherService.getGraderInfo(graderid  , bolumid, graderrole);
    }

//getSubSkillStepsBySubSkillid
    @GetMapping("/getskills/{bolumid}/{yilsayisi}/{secondgraderid}")
    public SkillsWithSecondGraderInfo getSkills(@PathVariable("bolumid") Long bolumid, @PathVariable("yilsayisi") Long yilsayisi, @PathVariable("secondgraderid") Long secondgraderid){
        return teacherService.getSkillsByBolumId(bolumid, yilsayisi, secondgraderid);
    }
    @GetMapping("/getsubskillsforstatistics/{studentid}/{bolumid}/{classlevel}/{skillid}/{baserole}")
    public SubskillsForStatistics getSubSkillsForStatistics(@PathVariable("studentid") Integer studentid, @PathVariable("bolumid") Integer bolumid, @PathVariable("classlevel") Integer classlevel, @PathVariable("skillid") Integer skillid, @PathVariable("baserole") Integer baserole){

        return teacherService.getSubSkillsForStatistics(studentid, bolumid, classlevel, skillid, baserole);
    }
    @GetMapping("/getsubskills/{studentid}/{bolumid}/{classlevel}/{skillid}")
    public List<SubSkillwithGrades> getSubSkills(@PathVariable("studentid") Integer studentid, @PathVariable("bolumid") Integer bolumid, @PathVariable("classlevel") Integer classlevel, @PathVariable("skillid") Integer skillid){
        return teacherService.getSubSkillsBySkillid(studentid, bolumid, classlevel, skillid);
    }

    @GetMapping("/getsubskillstepsforstatistics/{subskillid}/{studentid}/{bolumid}/{classlevel}/{baserole}")
    public SubskillStepsForStatistics getSubSkillStepsBySubSkillid(@PathVariable("subskillid") Long subskillid, @PathVariable("studentid") Long studentid, @PathVariable("bolumid") Long bolumid, @PathVariable("classlevel") Long classlevel, @PathVariable("baserole") Integer baserole){
        return teacherService.getSubSkillStepsForStatistics(subskillid, studentid, bolumid,classlevel, baserole);
    }

    @GetMapping("/getsubskillsteps/{subskillid}/{studentid}/{bolumid}/{classlevel}")
    public SubskillStepsAndEquipments getSubSkillSteps(@PathVariable("subskillid") Long subskillid, @PathVariable("studentid") Long studentid, @PathVariable("bolumid") Long bolumid, @PathVariable("classlevel") Long classlevel){
        return teacherService.getSubSkillStepsBySubSkillid(subskillid, studentid, bolumid,classlevel);
    }

    @GetMapping("/getstatistics/{studentid}/{bolumid}/{yilsayisi}")
    public StatisticsWithAverage getStatistics( @PathVariable("studentid") Long studentid, @PathVariable("bolumid") Long bolumid, @PathVariable("yilsayisi") Long yilsayisi){
        return teacherService.getStatistics( studentid, bolumid, yilsayisi );
    }

    @PostMapping("/savegrades")
    public void saveGrades(@RequestBody SaveGrades saveGrades){
         teacherService.saveGrades(saveGrades);
    }


}
