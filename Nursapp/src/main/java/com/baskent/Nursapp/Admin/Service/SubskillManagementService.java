package com.baskent.Nursapp.Admin.Service;

import com.baskent.Nursapp.Auth.config.ClientIpBean;
import com.baskent.Nursapp.DataAccessLayer.Action_RecordRepository;
import com.baskent.Nursapp.DataAccessLayer.SubSkillRepository;


import com.baskent.Nursapp.Entity.ActionRecords.Action_Records;


import com.baskent.Nursapp.Entity.Subskills;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class SubskillManagementService {

    @Autowired
    private SubSkillRepository subSkillRepository;

    @Autowired
    private Action_RecordRepository action_recordRepository;

    @Autowired
    private ClientIpBean clientIpBean;

    public void addSubSkill(Subskills subskill, Long admin, Long adminBolum) {
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + subskill.getName()  +" isimli alt beceriyi ekledi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());
        subSkillRepository.save(subskill);
    }

    public void addSubSkills(List<Subskills> subskills, Long admin, Long adminBolum) {
        for (Subskills subskill : subskills) {
            StringBuilder actionText = new StringBuilder(
                    "[" + admin + ", " + adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + subskill.getName() + " isimli alt beceriyi ekledi.");
            action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());
        }
        subSkillRepository.saveAll(subskills);
    }

    public List<Subskills> getSubSkills(Long skillId) {
        return subSkillRepository.getAllSubSkillsFromSkillId(skillId);
    }

    public Subskills getSubSkill(Long subskillId) {
        return subSkillRepository.findBySubSkillId(subskillId);
    }

    public void updateSubSkillName(String subskill, Long subskillId, Long admin, Long adminBolum ) {
        Subskills subskillToUpdate = subSkillRepository.findBySubSkillId(subskillId);
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + subSkillRepository.findBySubSkillId(subskillId).getName()  +" isimli alt beceriyi " + subskill+ " olarak güncelledi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());

        subskillToUpdate.setName(subskill);
        subSkillRepository.save(subskillToUpdate);
    }

    public void deleteSubSkill(Long subskillId, Long admin, Long adminBolum) {
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + subSkillRepository.findBySubSkillId(subskillId).getName()  +" isimli alt beceriyi sildi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());

        subSkillRepository.deleteById(subskillId);
    }

    @Transactional
    public void updateSubSkillsOrder(List<Long> orderList) {
        for (int i = 0; i < orderList.size(); i++) {
            Long sskillId = orderList.get(i);
            Subskills subskill = subSkillRepository.findById(sskillId).orElseThrow(
                    () -> new EntityNotFoundException("Skill not found with id " + sskillId));
            subskill.setStepNo(i + 1); // Sıralama 1'den başlar
            subSkillRepository.save(subskill);
        }
    }


}
