package com.baskent.Nursapp.Teacher.DTO;

import com.baskent.Nursapp.Entity.Performs.Performs;
import com.baskent.Nursapp.Entity.SubSkillSteps;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Date;
import java.time.OffsetDateTime;
import java.sql.Timestamp;
import java.time.Instant;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubSkillwithGrades {
    private Long subskillId; // BigInteger PostgreSQL'den dönen BIGINT için uygun
    private String name;
    private Integer skillId; // INTEGER PostgreSQL'den dönen INTEGER için uygun
    private Integer stepNo; // INTEGER PostgreSQL'den dönen INTEGER için uygun
    private Instant studentGradeDate; // TI MESTAMP PostgreSQL'den dönen TIMESTAMP için uygun
    private BigDecimal studentGrade; // DECIMAL PostgreSQL'den dönen DECIMAL için uygun
    private Instant teacherGradeDate; // TIMESTAMP PostgreSQL'den dönen TIMESTAMP için uygun
    private BigDecimal teacherGrade; // DECIMAL PostgreSQL'den dönen DECIMAL için uygun
    private Instant nurseGradeDate; // TIMESTAMP PostgreSQL'den dönen TIMESTAMP için uygun
    private BigDecimal nurseGrade; // DECIMAL PostgreSQL'den dönen DECIMAL için uygun
}