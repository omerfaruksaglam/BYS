package com.baskent.Nursapp.DataAccessLayer;

import com.baskent.Nursapp.Entity.Skills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillsRespository extends JpaRepository<Skills, Long> {
    @Query("SELECT k FROM Skills k WHERE k.bolumId = :bolumId ORDER BY k.stepNo ASC")// ORDER BY k.stepNo ASC
    List<Skills> getAllSkillsFromBolumId(@Param("bolumId") Long bolumId);


    @Query("SELECT COUNT(k) FROM Skills k WHERE k.bolumId = :bolumId")// ORDER BY k.stepNo ASC
    Long getSkillCountFromBolumId(@Param("bolumId") Long bolumId);

    Skills findBySkillId(Long skillId);

}
