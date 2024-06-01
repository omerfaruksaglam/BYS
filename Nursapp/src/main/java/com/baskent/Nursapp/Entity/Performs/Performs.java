package com.baskent.Nursapp.Entity.Performs;

import com.baskent.Nursapp.Entity.User.SuperKeyId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Date;


@Entity
@Table(name = "performs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(PerformsSuperKeyId.class)
public class Performs {
    @Id
    @Column(name = "student_id")
    private Long studentId;

    @Id
    @Column(name = "bolum_Id")
    private Long bolumId;

    @Id
    @Column(name = "subskill_steps_Id")
    private Long subskillStepsId;

    @Id
    @Column(name = "class_level")
    private Long classLevel;

    @Column(name = "perform_date")
    private ZonedDateTime performDate;

    @Column(name = "grade")
    private double grade;

}
