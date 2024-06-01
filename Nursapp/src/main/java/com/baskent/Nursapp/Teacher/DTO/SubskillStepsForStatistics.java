package com.baskent.Nursapp.Teacher.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubskillStepsForStatistics {
    List<SubskillStepsWithGrades> subskillSteps;
    List<SubskillStepsWithGrades> nullGrades;
    List<SubskillStepsWithGrades> zeroGrades;
    List<SubskillStepsWithGrades> firstGrades;
    List<SubskillStepsWithGrades> secondGrades;
    List<SubskillStepsWithGrades> thirdGrades;
    List<SubskillStepsWithGrades> forthGrades;
}
