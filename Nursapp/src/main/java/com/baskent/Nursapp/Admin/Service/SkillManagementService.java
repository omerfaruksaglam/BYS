package com.baskent.Nursapp.Admin.Service;

import java.time.Instant;
import java.util.List;

import com.baskent.Nursapp.Auth.config.ClientIpBean;
import com.baskent.Nursapp.DataAccessLayer.Action_RecordRepository;
import com.baskent.Nursapp.DataAccessLayer.SkillsRespository;

import com.baskent.Nursapp.Entity.ActionRecords.Action_Records;
import com.baskent.Nursapp.Entity.Skills;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SkillManagementService {

    @Autowired
    private SkillsRespository skillsRespository;

    @Autowired
    private Action_RecordRepository action_recordRepository;

    @Autowired private ClientIpBean clientIpBean;
    
    //crud operations
    public void addSkill(Skills skill, Long admin) {
        skillsRespository.save(skill);
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  skill.getBolumId() + "] numaralı userId ve bolumId ye sahip yönetici, " + skill.getNamee()  +" isimli beceriyi ekledi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid((long)admin).bolumId((long) skill.getBolumId()).actionText(actionText.toString()).build());
    }

    public void addSkills(List<Skills> skills, Long admin) {
        skillsRespository.saveAll(skills);
        for (Skills skill : skills) {
            StringBuilder actionText = new StringBuilder(
                    "[" + admin + ", "+  skill.getBolumId() + "] numaralı userId ve bolumId ye sahip yönetici, " + skill.getNamee()  +" isimli beceriyi ekledi.");
            action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid((long)admin).bolumId((long) skill.getBolumId()).actionText(actionText.toString()).build());
        }
    }

    public List<Skills> getSkills(Long bolumId) {
        return skillsRespository.getAllSkillsFromBolumId(bolumId);
    }

    public Skills getSkill(Long skillId) {
        return skillsRespository.findBySkillId(skillId);
    }

    public void updateSkillName(String skill, Long skillId, int admin) {
        Skills skillToUpdate = skillsRespository.findBySkillId(skillId);
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  skillToUpdate.getBolumId() + "] numaralı userId ve bolumId ye sahip yönetici, " + skillsRespository.findBySkillId(skillId).getNamee()  +" isimli beceriyi " + skill+ " olarak güncelledi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid((long)admin).bolumId((long) skillToUpdate.getBolumId()).actionText(actionText.toString()).build());

        skillToUpdate.setNamee(skill);
        skillsRespository.save(skillToUpdate);
    }

    public void updateSkillStepNo(int stepNo, Long skillId) {
        Skills skillToUpdate = skillsRespository.findBySkillId(skillId);
        skillToUpdate.setStepNo(stepNo);
        skillsRespository.save(skillToUpdate);
    }
    public void deleteSkill(Long skillId, Long admin, Long adminBolum) {
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + skillsRespository.findBySkillId(skillId).getNamee()  +" isimli beceriyi sildi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());
        skillsRespository.deleteById(skillId);
    }

    @Transactional
    public void updateSkillsOrder(List<Long> skills) {
        for (int i = 0; i < skills.size(); i++) {
            Long skillId = skills.get(i);
            Skills skill = skillsRespository.findById(skillId).orElseThrow(
                    () -> new EntityNotFoundException("Skill not found with id " + skillId));
            skill.setStepNo(i + 1); // Sıralama 1'den başlar
            skillsRespository.save(skill);
        }
    }

}
