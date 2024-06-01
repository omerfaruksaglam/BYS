package com.baskent.Nursapp.Admin.Controller;

import com.baskent.Nursapp.Admin.DTO.SkillNameUpdateDTO;
import com.baskent.Nursapp.Admin.Service.SubSkillStepsManagementService;
import com.baskent.Nursapp.Entity.SubSkillSteps;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nursapp/app/admin")
@RequiredArgsConstructor
public class SubSkillStepsManagementController {

    @Autowired
    private final SubSkillStepsManagementService subSkillStepsManagementService;

    //crud operations
    @GetMapping("/getsubskillssteps/{subskillid}")
    public ResponseEntity<List<SubSkillSteps>> getSubSkills(@PathVariable("subskillid") Long subskillid) {
        List<SubSkillSteps> subSkillSteps = subSkillStepsManagementService.getSubSkillsSteps(subskillid);
        if (subSkillSteps != null && !subSkillSteps.isEmpty()) {
            return new ResponseEntity<>(subSkillSteps, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getsubskillstep/{subskillstepid}")
    public ResponseEntity<SubSkillSteps> getSubSkill(@PathVariable("subskillstepid") Long subskillstepid) {
        SubSkillSteps subSkillSteps = subSkillStepsManagementService.getSubSkillSteps(subskillstepid);
        if (subSkillSteps != null) {
            return new ResponseEntity<>(subSkillSteps, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // add skill
    @PostMapping("/addsubskillstep/{admin}-{adminBolum}")
    public ResponseEntity<SubSkillSteps> addSubSkill(@RequestBody SubSkillSteps subskillsteps, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        subSkillStepsManagementService.addSubSkillSteps(subskillsteps, admin, adminBolum);
        return new ResponseEntity<>(subskillsteps, HttpStatus.CREATED);
    }

    // add skills
    @PostMapping("/addsubskillsteps/{admin}-{adminBolum}")
    public ResponseEntity<List<SubSkillSteps>> addSubSkills(@RequestBody List<SubSkillSteps> subskillsteps, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        subSkillStepsManagementService.addSubSkillStepss(subskillsteps, admin, adminBolum);
        return new ResponseEntity<>(subskillsteps, HttpStatus.CREATED);
    }

    // update skill name
    @PutMapping("/updatesubskillstep/{subskillstepid}/{weight}-{admin}-{adminBolum}")
    public ResponseEntity<SubSkillSteps> updateSubSkill(@RequestBody SkillNameUpdateDTO subskill , @PathVariable("subskillstepid") Long subskillstepid, @PathVariable("weight") Integer weight, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        subSkillStepsManagementService.updateSubSkillStepsName(subskill.getName(), subskillstepid, admin, adminBolum);
        subSkillStepsManagementService.UpdateSubSkillStepWeight(subskillstepid, weight, admin, adminBolum);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // delete skill
    @DeleteMapping("/deletesubskillstep/{subskillstepid}-{admin}-{adminBolum}")
    public ResponseEntity<SubSkillSteps> deleteSubSkill(@PathVariable("subskillstepid") Long subskillstepid, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        subSkillStepsManagementService.deleteSubSkillSteps(subskillstepid, admin, adminBolum);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/update-subskill-step-order")
    public ResponseEntity<?> updateSubSkillsOrder(@RequestBody List<Long> orderList) {
        subSkillStepsManagementService.updateSubSkillsOrder(orderList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}/update-header-status")
    public ResponseEntity<?> updateHeaderStatus(
            @PathVariable Long id,
            @RequestParam("head") Boolean head) {
        subSkillStepsManagementService.updateHeadStatus(id, head);
        return ResponseEntity.ok().build();
    }

}
