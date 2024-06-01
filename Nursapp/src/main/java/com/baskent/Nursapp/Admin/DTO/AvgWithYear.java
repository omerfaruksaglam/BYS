package com.baskent.Nursapp.Admin.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvgWithYear {
    private String academicYear;
    private String skillName;
    private BigDecimal averageGrade;
}
