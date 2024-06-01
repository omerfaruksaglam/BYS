package com.baskent.Nursapp.Entity.Statistics;

import com.baskent.Nursapp.Entity.Grades.GradesSuperKeyId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "statisticss")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(StatisticsSuperKeyId.class)
public class Statistics {
    @Id
    @Column(name = "student_id")
    private Long studentId;

    @Id
    @Column(name = "skill_id")
    private Long skillId;

    @Id
    @Column(name = "class_level")
    private Long classLevel;

    @Id
    @Column(name = "bolum_id")
    private Long bolumId;

    @Column(name = "success_rate")
    private Double successRate;


}
