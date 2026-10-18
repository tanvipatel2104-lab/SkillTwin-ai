
package com.skilltwin.model;

import java.math.BigDecimal;

public class Student {

    private long studentId;
    private String fullName;
    private int semester;
    private String email;
    private BigDecimal availableHoursPerDay;

    public Student() {
    }

    public Student(String fullName, int semester,
                   String email, BigDecimal availableHoursPerDay) {
        this.fullName = fullName;
        this.semester = semester;
        this.email = email;
        this.availableHoursPerDay = availableHoursPerDay;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getAvailableHoursPerDay() {
        return availableHoursPerDay;
    }

    public void setAvailableHoursPerDay(
            BigDecimal availableHoursPerDay) {
        this.availableHoursPerDay = availableHoursPerDay;
    }
}
