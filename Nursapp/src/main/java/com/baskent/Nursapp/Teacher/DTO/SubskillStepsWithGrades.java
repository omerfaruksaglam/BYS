package com.baskent.Nursapp.Teacher.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubskillStepsWithGrades {
    private Long subskillStepsId;
    private String name;
    private int stepNo;
    private Long subskillId;
    private Boolean isHead;
    private Instant performDate;
    private Double performGrade;
    private Instant gradeDateTeacher;
    private Long teacherId;
    private Double teacherGrade;
    private Instant gradeDateNurse;
    private Long nurseId;
    private Double nurseGrade;

}
