package com.skilltwin.dao;

import com.skilltwin.config.DatabaseConnection;
import com.skilltwin.model.Skill;
import com.skilltwin.model.Student;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimulationDAO {

    public Student getStudentById(long studentId)
            throws SQLException {

        String sql = "SELECT student_id, full_name, semester, "
                + "email, available_hours_per_day "
                + "FROM students WHERE student_id = ?";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, studentId);

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    Student student = new Student();

                    student.setStudentId(rs.getLong("student_id"));
                    student.setFullName(rs.getString("full_name"));
                    student.setSemester(rs.getInt("semester"));
                    student.setEmail(rs.getString("email"));
                    student.setAvailableHoursPerDay(
                            rs.getBigDecimal("available_hours_per_day"));

                    return student;
                }
            }
        }

        return null;
    }

    public List<Skill> getSkillsByStudentId(long studentId)
            throws SQLException {

        List<Skill> skills = new ArrayList<>();

        String sql = "SELECT skill_id, student_id, skill_name, "
                + "current_level, target_level "
                + "FROM skills WHERE student_id = ? "
                + "ORDER BY skill_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, studentId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    Skill skill = new Skill();

                    skill.setSkillId(rs.getLong("skill_id"));
                    skill.setStudentId(rs.getLong("student_id"));
                    skill.setSkillName(rs.getString("skill_name"));
                    skill.setCurrentLevel(
                            rs.getInt("current_level"));
                    skill.setTargetLevel(
                            rs.getInt("target_level"));

                    skills.add(skill);
                }
            }
        }

        return skills;
    }

    public long saveScenario(long studentId, String scenarioName,
                             int durationWeeks,
                             BigDecimal estimatedScore)
            throws SQLException {

        String sql = "INSERT INTO learning_scenarios "
                + "(student_id, scenario_name, duration_weeks, "
                + "estimated_score) VALUES (?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(
                             sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, studentId);
            statement.setString(2, scenarioName);
            statement.setInt(3, durationWeeks);
            statement.setBigDecimal(4, estimatedScore);

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getLong(1);
                }
            }
        }

        throw new SQLException("Scenario saved, but its ID was not returned.");
    }
}