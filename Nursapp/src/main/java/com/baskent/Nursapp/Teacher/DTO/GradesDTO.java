package com.baskent.Nursapp.Teacher.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GradesDTO {
    private Long subSkillStepsId;
    private double grade;
    private Long studentId;
    private Long bolumId;
    private Long sinif;
    private Long graderId;
}
