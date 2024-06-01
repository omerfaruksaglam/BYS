package com.baskent.Nursapp.Admin.Service;

import com.baskent.Nursapp.Auth.config.ClientIpBean;
import com.baskent.Nursapp.DataAccessLayer.Action_RecordRepository;
import com.baskent.Nursapp.DataAccessLayer.SubSkillStepsRepository;

import com.baskent.Nursapp.Entity.ActionRecords.Action_Records;


import com.baskent.Nursapp.Entity.SubSkillSteps;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class SubSkillStepsManagementService {

    @Autowired
    private SubSkillStepsRepository subSkillStepsRepository;
    @Autowired
    private Action_RecordRepository action_recordRepository;

    @Autowired
    private ClientIpBean clientIpBean;

    //crud operations
    public void addSubSkillSteps(SubSkillSteps subSkillStep, Long admin, Long adminBolum) {
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + subSkillStep.getName()  +" isimli alt beceri basamağını ekledi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());
        subSkillStepsRepository.save(subSkillStep);
    }

    public List<SubSkillSteps> getSubSkillsSteps(Long subSkillId) {
        return subSkillStepsRepository.getAllSubSkillStepsFromSubSkillId(subSkillId);
    }

    public SubSkillSteps getSubSkillSteps(Long subskillStepsId) {
        return subSkillStepsRepository.findBysubskillStepsId(subskillStepsId);
    }

    public void updateSubSkillStepsName(String subskillstep, Long subskillStepsId, Long admin, Long adminBolum) {
        SubSkillSteps subskillToUpdate = subSkillStepsRepository.findBysubskillStepsId(subskillStepsId);
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + subSkillStepsRepository.findBysubskillStepsId(subskillStepsId).getName()  +" isimli alt beceri basamağını " + subskillstep+ " olarak güncelledi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());

        subskillToUpdate.setName(subskillstep);
        subSkillStepsRepository.save(subskillToUpdate);
    }

    public void deleteSubSkillSteps(Long subskillStepsId, Long admin, Long adminBolum) {
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + subSkillStepsRepository.findBysubskillStepsId(subskillStepsId).getName()  +" isimli alt beceri basamağını sildi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());

        subSkillStepsRepository.deleteById(subskillStepsId);
    }

    @Transactional
    public void updateSubSkillsOrder(List<Long> orderList) {
        for (int i = 0; i < orderList.size(); i++) {
            Long sskillId = orderList.get(i);
            SubSkillSteps subskill = subSkillStepsRepository.findById(sskillId).orElseThrow(
                    () -> new EntityNotFoundException("Skill not found with id " + sskillId));
            subskill.setStepNo(i + 1); // Sıralama 1'den başlar
            subSkillStepsRepository.save(subskill);
        }
    }

    @Transactional
    public void updateHeadStatus(Long id, Boolean head) {
        SubSkillSteps step = subSkillStepsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubSkillStep not found"));
        step.setHead(head);
        subSkillStepsRepository.save(step);
    }

    public ResponseEntity<SubSkillSteps> UpdateSubSkillStepWeight(Long stepId, int weight, Long admin, Long adminBolum) {
        SubSkillSteps subskill = subSkillStepsRepository.findBysubskillStepsId(stepId);
        if (subskill != null) {
            StringBuilder actionText = new StringBuilder(
                    "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + subSkillStepsRepository.findBysubskillStepsId(stepId).getName()  +" isimli alt beceri basamağının ağırlığını " + weight+ " olarak güncelledi.");
            action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());

            subskill.setWeight(weight);
            subSkillStepsRepository.save(subskill);
            return new ResponseEntity<>(subskill, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public void addSubSkillStepss(List<SubSkillSteps> subskillsteps, Long admin, Long adminBolum) {
        subSkillStepsRepository.saveAll(subskillsteps);
        for (SubSkillSteps subskill : subskillsteps) {
            StringBuilder actionText = new StringBuilder(
                    "[" + admin + ", " + adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + subskill.getName() + " isimli alt beceri basamağını ekledi.");
            action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());
        }
    }
}
