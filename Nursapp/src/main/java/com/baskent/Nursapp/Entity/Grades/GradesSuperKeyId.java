package com.baskent.Nursapp.Entity.Grades;


import java.io.Serializable;
import java.util.Objects;

public class GradesSuperKeyId implements Serializable {
    private Long studentId;
    private Long studentBolumId;
    private Long subskillStepsId;
    private Long classLevel;
    private Long graderRole;


    public GradesSuperKeyId(){}
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GradesSuperKeyId that = (GradesSuperKeyId) o;
        return Objects.equals(studentId, that.studentId) &&
                Objects.equals(studentBolumId, that.studentBolumId) &&
                Objects.equals(subskillStepsId, that.subskillStepsId) &&
                Objects.equals(classLevel, that.classLevel) &&
                Objects.equals(graderRole, that.graderRole);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId, studentBolumId, subskillStepsId, classLevel, graderRole);
    }
}
