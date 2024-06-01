package com.baskent.Nursapp.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subskillsteps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubSkillSteps {
    @Id
    @Column(name = "subskill_steps_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subskillStepsId;

    @Column(name = "name")
    private String name;

    @Column(name = "step_no")
    private int stepNo;

    @Column(name = "subskill_id")
    private Long subSkillId;

    @Column(name = "ishead")
    private boolean isHead;

    @Column(name = "weight")
    private int weight;
}
