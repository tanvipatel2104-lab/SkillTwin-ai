
package com.skilltwin.dao;

import com.skilltwin.config.DatabaseConnection;
import com.skilltwin.model.LearningScenario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LearningScenarioDAO {

    public List<LearningScenario> getScenariosByStudentId(
            long studentId) throws SQLException {

        List<LearningScenario> scenarios = new ArrayList<>();

        String sql = "SELECT scenario_id, student_id, scenario_name, "
                + "duration_weeks, estimated_score "
                + "FROM learning_scenarios "
                + "WHERE student_id = ? "
                + "ORDER BY duration_weeks, scenario_id";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, studentId);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    LearningScenario scenario =
                            new LearningScenario();

                    scenario.setScenarioId(rs.getLong("scenario_id"));
                    scenario.setStudentId(rs.getLong("student_id"));
                    scenario.setScenarioName(rs.getString("scenario_name"));
                    scenario.setDurationWeeks(rs.getInt("duration_weeks"));
                    scenario.setEstimatedScore(
                            rs.getBigDecimal("estimated_score"));

                    scenarios.add(scenario);
                }
            }
        }

        return scenarios;
    }
}
