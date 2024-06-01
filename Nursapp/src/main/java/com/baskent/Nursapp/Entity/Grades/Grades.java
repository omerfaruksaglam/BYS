package com.baskent.Nursapp.Entity.Grades;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "grades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(GradesSuperKeyId.class)
public class Grades {
    @Id
    @Column(name = "student_id")
    private Long studentId;

    @Id
    @Column(name = "student_bolum_id")
    private Long studentBolumId;

    @Id
    @Column(name = "subskill_steps_id")
    private Long subskillStepsId;

    @Id
    @Column(name = "class_level")
    private Long classLevel;

    @Id
    @Column(name = "grader_role")
    private Long graderRole;

    @Column(name = "grader_id")
    private Long graderId;

    @Column(name = "grader_bolum_id")
    private Long graderBolumId;

    @Column(name = "grade_date")
    private ZonedDateTime gradeDate;

    @Column(name = "grade")
    private double grade;
}
