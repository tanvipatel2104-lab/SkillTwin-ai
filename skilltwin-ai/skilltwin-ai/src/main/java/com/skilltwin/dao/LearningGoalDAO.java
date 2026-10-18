package com.skilltwin.dao;

import com.skilltwin.config.DatabaseConnection;
import com.skilltwin.model.LearningGoal;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LearningGoalDAO {

    public long addGoal(LearningGoal goal) throws SQLException {

        String studentCheck =
                "SELECT 1 FROM students WHERE student_id = ?";

        String insertSQL =
                "INSERT INTO learning_goals "
                + "(student_id, goal_title, description, target_date, status) "
                + "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            // Verify that the student exists.
            try (PreparedStatement check =
                         connection.prepareStatement(studentCheck)) {

                check.setLong(1, goal.getStudentId());

                try (ResultSet resultSet = check.executeQuery()) {
                    if (!resultSet.next()) {
                        throw new SQLException(
                                "Student ID " + goal.getStudentId()
                                + " does not exist.");
                    }
                }
            }

            // Save the goal.
            try (PreparedStatement statement =
                         connection.prepareStatement(
                                 insertSQL,
                                 PreparedStatement.RETURN_GENERATED_KEYS)) {

                statement.setLong(1, goal.getStudentId());
                statement.setString(2, goal.getGoalTitle());

                if (goal.getDescription() == null
                        || goal.getDescription().isBlank()) {
                    statement.setNull(3, java.sql.Types.VARCHAR);
                } else {
                    statement.setString(3, goal.getDescription());
                }

                if (goal.getTargetDate() == null) {
                    statement.setNull(4, java.sql.Types.DATE);
                } else {
                    statement.setDate(
                            4, Date.valueOf(goal.getTargetDate()));
                }

                statement.setString(5, goal.getStatus());

                statement.executeUpdate();

                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (keys.next()) {
                        return keys.getLong(1);
                    }
                }
            }
        }

        throw new SQLException("Goal was saved, but its ID was not returned.");
    }

    public List<LearningGoal> getGoalsByStudentId(long studentId)
            throws SQLException {

        List<LearningGoal> goals = new ArrayList<>();

        String sql =
                "SELECT goal_id, student_id, goal_title, description, "
                + "target_date, status "
                + "FROM learning_goals "
                + "WHERE student_id = ? ORDER BY goal_id";

        try (Connection connection =
                     DatabaseConnection.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setLong(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    LearningGoal goal = new LearningGoal();

                    goal.setGoalId(resultSet.getLong("goal_id"));
                    goal.setStudentId(resultSet.getLong("student_id"));
                    goal.setGoalTitle(resultSet.getString("goal_title"));
                    goal.setDescription(
                            resultSet.getString("description"));

                    Date date = resultSet.getDate("target_date");
                    goal.setTargetDate(
                            date == null ? null : date.toLocalDate());

                    goal.setStatus(resultSet.getString("status"));

                    goals.add(goal);
                }
            }
        }

        return goals;
    }


public boolean updateGoalStatus(long goalId, long studentId, String status)
        throws SQLException {

    String sql = "UPDATE learning_goals "
            + "SET status = ? "
            + "WHERE goal_id = ? AND student_id = ?";

    try (Connection connection = DatabaseConnection.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setString(1, status);
        statement.setLong(2, goalId);
        statement.setLong(3, studentId);

        return statement.executeUpdate() == 1;
    }
}

}
