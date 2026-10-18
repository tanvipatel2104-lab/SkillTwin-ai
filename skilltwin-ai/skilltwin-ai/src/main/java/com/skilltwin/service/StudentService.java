
package com.skilltwin.service;

import com.skilltwin.dao.StudentDAO;
import com.skilltwin.model.Student;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO;

    public StudentService() {
        this.studentDAO = new StudentDAO();
    }

    public boolean registerStudent(Student student)
            throws SQLException {

        if (student.getFullName() == null
                || student.getFullName().isBlank()) {
            throw new IllegalArgumentException(
                    "Student name cannot be empty.");
        }

        if (student.getFullName().length() > 100) {
            throw new IllegalArgumentException(
                    "Student name cannot exceed 100 characters.");
        }

        if (student.getSemester() < 1
                || student.getSemester() > 12) {
            throw new IllegalArgumentException(
                    "Semester must be between 1 and 12.");
        }

        String email = student.getEmail();

        if (email != null && !email.isBlank()) {
            email = email.trim();

            if (email.length() > 150
                    || !email.matches(
                    "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                throw new IllegalArgumentException(
                        "Please enter a valid email address.");
            }

            student.setEmail(email);
        } else {
            student.setEmail(null);
        }

        BigDecimal hours = student.getAvailableHoursPerDay();

        if (hours != null
                && (hours.compareTo(BigDecimal.ZERO) < 0
                || hours.compareTo(new BigDecimal("24")) > 0)) {
            throw new IllegalArgumentException(
                    "Study hours must be between 0 and 24.");
        }

        return studentDAO.registerStudent(student);
    }

    public List<Student> getAllStudents()
            throws SQLException {
        return studentDAO.getAllStudents();
    }
}
