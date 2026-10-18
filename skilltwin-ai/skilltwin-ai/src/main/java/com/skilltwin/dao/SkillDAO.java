package com.skilltwin.dao;

import com.skilltwin.config.DatabaseConnection;
import com.skilltwin.model.Skill;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SkillDAO {

    public long addSkill(Skill skill) throws SQLException {

        String studentCheck =
                "SELECT 1 FROM students WHERE student_id = ?";

        String insertSQL =
                "INSERT INTO skills "
                + "(student_id, skill_name, current_level, target_level) "
                + "VALUES (?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            // Confirm that the student exists.
            try (PreparedStatement checkStatement =
                         connection.prepareStatement(studentCheck)) {

                checkStatement.setLong(1, skill.getStudentId());

                try (ResultSet resultSet = checkStatement.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new SQLException(
                                "Student ID " + skill.getStudentId()
                                + " does not exist."
                        );
                    }
                }
            }

            // Insert the student's skill.
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 insertSQL,
                                 PreparedStatement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, skill.getStudentId());
                statement.setString(2, skill.getSkillName());
                statement.setInt(3, skill.getCurrentLevel());
                statement.setInt(4, skill.getTargetLevel());

                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getLong(1);
                    }
                }
            }
        }

        throw new SQLException("Skill saved, but its ID was not returned.");
    }

    public List<Skill> getSkillsByStudentId(long studentId)
            throws SQLException {

        List<Skill> skills = new ArrayList<>();

        String sql =
                "SELECT skill_id, student_id, skill_name, "
                + "current_level, target_level "
                + "FROM skills WHERE student_id = ? "
                + "ORDER BY skill_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Skill skill = new Skill();

                    skill.setSkillId(resultSet.getLong("skill_id"));
                    skill.setStudentId(resultSet.getLong("student_id"));
                    skill.setSkillName(resultSet.getString("skill_name"));
                    skill.setCurrentLevel(
                            resultSet.getInt("current_level"));
                    skill.setTargetLevel(
                            resultSet.getInt("target_level"));

                    skills.add(skill);
                }
            }
        }

        return skills;
    }
}