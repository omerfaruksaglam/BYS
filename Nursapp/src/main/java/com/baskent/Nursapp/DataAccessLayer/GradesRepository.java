package com.baskent.Nursapp.DataAccessLayer;

import com.baskent.Nursapp.Entity.Grades.Grades;
import com.baskent.Nursapp.Entity.Grades.GradesSuperKeyId;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;

@Repository
public interface GradesRepository extends JpaRepository<Grades, GradesSuperKeyId> {
    /*@Modifying
    @Query(value = "INSERT INTO grades (student_id, student_bolum_id, grader_id, grader_bolum_id, subskill_steps_id, class_level, grader_role, grade_date, grade) " +
            "VALUES (:studentId, :studentBolumId, :graderId, :graderBolumId, :subskillStepsId, :classLevel, 0, CURRENT_DATE, :grade) " +
            "ON CONFLICT (student_id, subskill_steps_id, class_level, student_bolum_id, grader_role) " +
            "DO UPDATE SET grade = EXCLUDED.grade", nativeQuery = true)
    @Transactional
    void upsertGrade(@Param("studentId") Long studentId, @Param("studentBolumId") Long studentBolumId, @Param("graderId") Long graderId, @Param("graderBolumId") Long graderBolumId, @Param("subskillStepsId") Long subskillStepsId, @Param("classLevel") Long classLevel, @Param("grade") double grade);


     */
    @Modifying
    @Query("UPDATE Grades g SET g.grade = :grade, g.graderId = :graderId, g.gradeDate = :gradeDate WHERE g.studentId = :studentId AND g.studentBolumId = :studentBolumId AND g.subskillStepsId = :subSkillStepsId AND g.classLevel = :classLevel AND g.graderRole = :graderRole ")
    @Transactional
    void updateGrade(@Param("studentId") Long studentId, @Param("studentBolumId") Long studentBolumId, @Param("subSkillStepsId") Long subSkillStepsId, @Param("classLevel") Long classLevel, @Param("graderRole") Long graderRole, @Param("grade") double grade, @Param("graderId") Long graderId,  @Param("gradeDate") ZonedDateTime gradeDate);
    @Query("SELECT COUNT(g) > 0 FROM Grades g WHERE g.studentId = :studentId AND g.studentBolumId = :bolumId AND g.subskillStepsId = :subSkillStepsId AND g.classLevel = :sinif AND g.graderRole = :graderRole")
    boolean existsByCustomId(@Param("studentId") Long studentId, @Param("bolumId") Long bolumId, @Param("subSkillStepsId") Long subSkillStepsId, @Param("sinif") Long sinif, @Param("graderRole") Long graderRole);

}

