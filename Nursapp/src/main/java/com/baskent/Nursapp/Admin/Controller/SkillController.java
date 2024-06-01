package com.baskent.Nursapp.Admin.Controller;

import java.time.Instant;
import java.util.List;

import com.baskent.Nursapp.Admin.DTO.SkillNameUpdateDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.baskent.Nursapp.Admin.Service.SkillManagementService;
import com.baskent.Nursapp.Entity.Skills;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/nursapp/app/admin")
@RequiredArgsConstructor
public class SkillController {

    private final SkillManagementService skillManagementService;

    @GetMapping("/getskills/{bolumid}")
    public ResponseEntity<List<Skills>> getSkills(@PathVariable("bolumid") Long bolumid) {
        List<Skills> skills = skillManagementService.getSkills(bolumid);
        if (skills != null && !skills.isEmpty()) {
            return new ResponseEntity<>(skills, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getskill/{skillid}")
    public ResponseEntity<Skills> getSkill(@PathVariable("skillid") Long skillid) {
        Skills skill = skillManagementService.getSkill(skillid);
        if (skill != null) {
            return new ResponseEntity<>(skill, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // add skill
    @PostMapping("/addskill/{admin}")
    public ResponseEntity<Skills> addSkill(@RequestBody Skills skill, @PathVariable("admin") Long admin) {
        skillManagementService.addSkill(skill, admin);
        return new ResponseEntity<>(skill, HttpStatus.CREATED);
    }

    // add skills
    @PostMapping("/addskills/{admin}")
    public ResponseEntity<List<Skills>> addSkills(@RequestBody List<Skills> skills, @PathVariable("admin") Long admin) {
        skillManagementService.addSkills(skills, admin);
        return new ResponseEntity<>(skills, HttpStatus.CREATED);
    }

    // update skill name
    @PutMapping("/updateskill/{skillid}-{admin}")
    public ResponseEntity<Skills> updateSkill(@RequestBody SkillNameUpdateDTO skill , @PathVariable("skillid") Long skillid, @PathVariable("admin") int admin){
        skillManagementService.updateSkillName(skill.getName(), skillid, admin);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete skill
    @DeleteMapping("/deleteskill/{skillid}-{admin}-{adminBolum}")
    public ResponseEntity<Skills> deleteSkill(@PathVariable("skillid") Long skillid, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        skillManagementService.deleteSkill(skillid, admin, adminBolum);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update-skill-order")
    public ResponseEntity<?> updateSkillsOrder(@RequestBody List<Long> skills) {
        skillManagementService.updateSkillsOrder(skills);
        return ResponseEntity.ok().body("Sıralama başarıyla güncellendi.");
    }

}
