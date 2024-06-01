package com.baskent.Nursapp.Admin.Controller;

import com.baskent.Nursapp.Admin.DTO.SkillNameUpdateDTO;
import com.baskent.Nursapp.Admin.Service.SubskillManagementService;
import com.baskent.Nursapp.Entity.Subskills;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nursapp/app/admin")
@RequiredArgsConstructor
public class SubskillManagementController {

    @Autowired
    private final SubskillManagementService subskillManagementService;

    //crud operations
    @GetMapping("/getsubskills/{skillid}")
    public ResponseEntity<List<Subskills>> getSubSkills(@PathVariable("skillid") Long skillId) {
        List<Subskills> subskills = subskillManagementService.getSubSkills(skillId);
        if (subskills != null && !subskills.isEmpty()) {
            return new ResponseEntity<>(subskills, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getsubskill/{subskillid}")
    public ResponseEntity<Subskills> getSubSkill(@PathVariable("skillid") Long subskillId) {
        Subskills subskill = subskillManagementService.getSubSkill(subskillId);
        if (subskill != null) {
            return new ResponseEntity<>(subskill, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // add skill
    @PostMapping("/addsubskill/{admin}-{adminBolum}")
    public ResponseEntity<Subskills> addSubSkill(@RequestBody Subskills subskill, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        subskillManagementService.addSubSkill(subskill, admin, adminBolum);
        return new ResponseEntity<>(subskill, HttpStatus.CREATED);
    }

    // add skills
    @PostMapping("/addsubskills/{admin}-{adminBolum}")
    public ResponseEntity<List<Subskills>> addSubSkills(@RequestBody List<Subskills> subskills, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        subskillManagementService.addSubSkills(subskills, admin, adminBolum);
        return new ResponseEntity<>(subskills, HttpStatus.CREATED);
    }

    // update skill name
    @PutMapping("/updatesubskill/{subskillid}-{admin}-{adminBolum}")
    public ResponseEntity<Subskills> updateSubSkill(@RequestBody SkillNameUpdateDTO subskill, @PathVariable("subskillid") Long subskillId, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum){
        subskillManagementService.updateSubSkillName(subskill.getName(), subskillId, admin, adminBolum);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete skill
    @DeleteMapping("/deletesubskill/{subskillid}-{admin}-{adminBolum}")
    public ResponseEntity<Subskills> deleteSubSkill(@PathVariable("subskillid") Long subskillid, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        subskillManagementService.deleteSubSkill(subskillid, admin, adminBolum);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update-subskill-order")
    public ResponseEntity<?> updateSubSkillsOrder(@RequestBody List<Long> orderList) {
        subskillManagementService.updateSubSkillsOrder(orderList);
        return ResponseEntity.ok().body("Sıralama başarıyla güncellendi.");
    }



}
