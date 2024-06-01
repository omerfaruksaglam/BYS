package com.baskent.Nursapp.DataAccessLayer;

import com.baskent.Nursapp.Entity.Equipments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipments, Long> {

    @Query("SELECT k FROM Equipments k WHERE k.subskillId = :subskillId ")
    List<Equipments> getAllEquipmentsFromSubskillId(@Param("subskillId") Long subskillId);

    Equipments findByEquipmentId(Long equipmentId);
}
