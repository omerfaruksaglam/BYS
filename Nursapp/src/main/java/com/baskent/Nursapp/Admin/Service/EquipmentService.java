package com.baskent.Nursapp.Admin.Service;

import com.baskent.Nursapp.Auth.config.ClientIpBean;
import com.baskent.Nursapp.DataAccessLayer.Action_RecordRepository;
import com.baskent.Nursapp.DataAccessLayer.EquipmentRepository;

import com.baskent.Nursapp.Entity.ActionRecords.Action_Records;

import com.baskent.Nursapp.Entity.Equipments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepository equipmentsRepository;
    @Autowired
    private Action_RecordRepository action_recordRepository;

    @Autowired
    private ClientIpBean clientIpBean;

    public List<Equipments> getEquipments(Long subskillId) {
        return equipmentsRepository.getAllEquipmentsFromSubskillId(subskillId);
    }

    public Equipments getEquipment(Long equipmentId) {
        return equipmentsRepository.findByEquipmentId(equipmentId);
    }

    public void addEquipment(Equipments equipments, Long admin, Long adminBolum) {
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + equipments.getName()  +" isimli ekipmanı ekledi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());

        equipmentsRepository.save(equipments);
    }

    public void addEquipments(List<Equipments> equipments, Long admin, Long adminBolum) {
        equipmentsRepository.saveAll(equipments);
        for (Equipments equipment : equipments) {
            StringBuilder actionText = new StringBuilder(
                    "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + equipment.getName()  +" isimli ekipmanı ekledi.");
            action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(1L).bolumId(1L).actionText(actionText.toString()).build());
        }
    }

    public void updateEquipmentName(String name, Long equipmentId, Long admin, Long adminBolum) {
        Equipments equipments = equipmentsRepository.findByEquipmentId(equipmentId);
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + equipmentsRepository.findByEquipmentId(equipmentId).getName()  +" isimli ekipmanı " + name + " olarak güncelledi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());

        equipments.setName(name);
        equipmentsRepository.save(equipments);
    }

    public void deleteEquipment(Long equipmentId, Long admin, Long adminBolum) {
        StringBuilder actionText = new StringBuilder(
                "[" + admin + ", "+  adminBolum + "] numaralı userId ve bolumId ye sahip yönetici, " + equipmentsRepository.findByEquipmentId(equipmentId).getName()  +" isimli ekipmanı sildi.");
        action_recordRepository.save(Action_Records.builder().actionTime(Instant.now()).ipAddress(clientIpBean.getClientIp()).userid(admin).bolumId(adminBolum).actionText(actionText.toString()).build());

        equipmentsRepository.deleteById(equipmentId);
    }

}
