package com.baskent.Nursapp.Admin.Controller;

import com.baskent.Nursapp.Admin.DTO.SkillNameUpdateDTO;
import com.baskent.Nursapp.Admin.Service.EquipmentService;
import com.baskent.Nursapp.Entity.Equipments;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/nursapp/app/admin")
public class EquipmentController {

    @Autowired
    private final EquipmentService equipmentService;

    @GetMapping("/equipments/{subskillId}")
    public List<Equipments> getEquipments(@PathVariable Long subskillId) {
        return equipmentService.getEquipments(subskillId);
    }

    @GetMapping("/equipment/{equipmentId}")
    public Equipments getEquipment(@PathVariable Long equipmentId) {
        return equipmentService.getEquipment(equipmentId);
    }

    @PostMapping("/addequipment/{admin}-{adminBolum}")
    public void addEquipment(@RequestBody Equipments equipments, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        equipmentService.addEquipment(equipments, admin, adminBolum);
    }

    @PostMapping("/addequipments/{admin}-{adminBolum}")
    public void addEquipments(@RequestBody List<Equipments> equipments, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        equipmentService.addEquipments(equipments, admin, adminBolum);
    }

    @PutMapping("/updateequipment/{equipmentId}-{admin}-{adminBolum}")
    public ResponseEntity<Equipments> updateEquipmentName(@RequestBody SkillNameUpdateDTO name, @PathVariable Long equipmentId, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        equipmentService.updateEquipmentName(name.getName(), equipmentId, admin, adminBolum);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/deleteequipment/{equipmentId}-{admin}-{adminBolum}")
    public void deleteEquipment(@PathVariable Long equipmentId, @PathVariable("admin") Long admin, @PathVariable("adminBolum") Long adminBolum) {
        equipmentService.deleteEquipment(equipmentId, admin, adminBolum);
    }
}
