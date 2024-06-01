package com.baskent.Nursapp.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "subskills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subskills {
    @Id
    @Column(name = "subskill_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subSkillId;

    @Column(name = "name")
    private String name;

    @Column(name = "skill_id")
    private int skillId;

    @Column(name = "step_no")
    private int stepNo;


}
