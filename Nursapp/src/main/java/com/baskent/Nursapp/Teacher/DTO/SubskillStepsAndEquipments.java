package com.baskent.Nursapp.Teacher.DTO;

import com.baskent.Nursapp.Entity.Equipments;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubskillStepsAndEquipments {
    List<SubskillStepsWithGrades> subskillStepsWithGrades;
    List<Equipments> equipments;
}
