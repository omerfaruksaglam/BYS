package com.baskent.Nursapp.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "secondgrader")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecondGrader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "second_grader_id")
    private Long secondGraderId;

    @Column(name = "second_grader_name")
    private String secondGraderName;
}
