package com.baskent.Nursapp.Teacher.DTO;

import com.baskent.Nursapp.Entity.SecondGrader;
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
public class SkillsWithSecondGraderInfo {
    List<Skills> skills;
    SecondGrader secondGrader;
    Boolean secondGraderNotVerebilirmi;
}
