package com.baskent.Nursapp.DataAccessLayer;


import com.baskent.Nursapp.Entity.Skills;
import com.baskent.Nursapp.Entity.Subskills;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubSkillRepository extends JpaRepository<Subskills, Long> {
    @Query("SELECT k FROM Subskills k WHERE k.skillId = :skillId ORDER BY k.stepNo ASC")
    List<Subskills> getAllSubSkillsFromSkillId(@Param("skillId") Long skillId );

    Subskills findBySubSkillId(Long subskillId);
}
