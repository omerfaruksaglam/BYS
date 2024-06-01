package com.baskent.Nursapp.DataAccessLayer;

import com.baskent.Nursapp.Entity.Performs.Performs;
import com.baskent.Nursapp.Entity.Performs.PerformsSuperKeyId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface PerformsRepository extends JpaRepository<Performs, PerformsSuperKeyId> {
    @Modifying
    @Query("UPDATE Performs g SET g.grade = :grade, g.performDate = :gradeDate WHERE g.studentId = :studentId AND g.bolumId = :bolumId AND g.subskillStepsId = :subSkillStepsId AND g.classLevel = :classLevel")
    @Transactional
    void updateGrade(@Param("studentId") Long studentId, @Param("bolumId") Long bolumId, @Param("subSkillStepsId") Long subSkillStepsId, @Param("classLevel") Long classLevel, @Param("grade") double grade, @Param("gradeDate") ZonedDateTime gradeDate);

    @Query("SELECT COUNT(g) > 0 FROM Performs g WHERE g.studentId = :studentId AND g.bolumId = :bolumId AND g.subskillStepsId = :subSkillStepsId AND g.classLevel = :sinif")
    boolean existsByCustomId(@Param("studentId") Long studentId, @Param("bolumId") Long bolumId, @Param("subSkillStepsId") Long subSkillStepsId, @Param("sinif") Long sinif);

}
