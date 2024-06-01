package com.baskent.Nursapp.DataAccessLayer;


import com.baskent.Nursapp.Entity.SubSkillSteps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubSkillStepsRepository extends JpaRepository<SubSkillSteps,Long> {
    @Query("SELECT k FROM SubSkillSteps k WHERE k.subSkillId = :subskillId order by k.stepNo asc")
    List<SubSkillSteps> getAllSubSkillStepsFromSubSkillId(@Param("subskillId") Long subskillId);

    SubSkillSteps findBysubskillStepsId(Long subskillStepsId);

    @Modifying
    @Query("UPDATE SubSkillSteps s SET s.isHead = :head WHERE s.subskillStepsId= :id")
    int updateHeaderStatus(@Param("id") Long id, @Param("head") Boolean head);
}
