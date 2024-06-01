package com.baskent.Nursapp.Teacher.DTO;

import com.baskent.Nursapp.Entity.Skills;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubskillsForStatistics {
    List<SubSkillwithGrades> subskills;
    List<SubSkillwithGrades> nullGrades;
    List<SubSkillwithGrades> zeroGrades;
    List<SubSkillwithGrades> firstGrades;
    List<SubSkillwithGrades> secondGrades;
    List<SubSkillwithGrades> thirdGrades;
    List<SubSkillwithGrades> forthGrades;


}
