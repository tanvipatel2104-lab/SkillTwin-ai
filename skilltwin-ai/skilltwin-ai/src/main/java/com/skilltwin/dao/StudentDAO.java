
package com.skilltwin.dao;

import com.skilltwin.config.DatabaseConnection;
import com.skilltwin.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    public boolean registerStudent(Student student)
            throws SQLException {

        String sql = """
                INSERT INTO students
                (full_name, semester, email, available_hours_per_day)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql,
                             PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, student.getFullName());
            statement.setInt(2, student.getSemester());

            if (student.getEmail() == null
                    || student.getEmail().isBlank()) {
                statement.setNull(
                        3, java.sql.Types.VARCHAR);
            } else {
                statement.setString(3, student.getEmail());
            }

            if (student.getAvailableHoursPerDay() == null) {
                statement.setNull(
                        4, java.sql.Types.DECIMAL);
            } else {
                statement.setBigDecimal(
                        4, student.getAvailableHoursPerDay());
            }

            int rows = statement.executeUpdate();

            if (rows > 0) {
                try (ResultSet keys =
                             statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        student.setStudentId(keys.getLong(1));
                    }
                }
                return true;
            }

            return false;
        }
    }

    public List<Student> getAllStudents()
            throws SQLException {

        List<Student> students = new ArrayList<>();

        String sql = """
                SELECT student_id, full_name, semester,
                       email, available_hours_per_day
                FROM students
                ORDER BY student_id
                """;

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Student student = new Student();

                student.setStudentId(
                        resultSet.getLong("student_id"));

                student.setFullName(
                        resultSet.getString("full_name"));

                student.setSemester(
                        resultSet.getInt("semester"));

                student.setEmail(
                        resultSet.getString("email"));

                student.setAvailableHoursPerDay(
                        resultSet.getBigDecimal(
                                "available_hours_per_day"));

                students.add(student);
            }
        }

        return students;
    }
}
