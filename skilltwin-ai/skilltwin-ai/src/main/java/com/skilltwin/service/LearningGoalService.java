
package com.skilltwin.service;

import com.skilltwin.dao.LearningGoalDAO;
import com.skilltwin.model.LearningGoal;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class LearningGoalService {

    private final LearningGoalDAO goalDAO;

    private static final Set<String> ALLOWED_STATUSES =
            Set.of("PENDING", "IN_PROGRESS", "COMPLETED");

    public LearningGoalService() {
        this.goalDAO = new LearningGoalDAO();
    }

    public long addGoal(long studentId, String title,
                        String description, LocalDate targetDate)
            throws SQLException {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero.");
        }

        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException(
                    "Goal title cannot be empty.");
        }

        title = title.trim();

        if (title.length() > 200) {
            throw new IllegalArgumentException(
                    "Goal title cannot exceed 200 characters.");
        }

        if (description != null && description.isBlank()) {
            description = null;
        }

        if (targetDate != null && targetDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Target date cannot be in the past.");
        }

        LearningGoal goal = new LearningGoal(
                studentId, title, description, targetDate, "PENDING");

        return goalDAO.addGoal(goal);
    }

    public List<LearningGoal> getGoalsByStudentId(long studentId)
            throws SQLException {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero.");
        }

        return goalDAO.getGoalsByStudentId(studentId);
    }

    public String validateStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException(
                    "Status cannot be empty.");
        }

        String normalizedStatus = status.trim().toUpperCase();

        if (!ALLOWED_STATUSES.contains(normalizedStatus)) {
            throw new IllegalArgumentException(
                    "Status must be PENDING, IN_PROGRESS, or COMPLETED.");
        }

        return normalizedStatus;
    }

    public boolean updateGoalStatus(long goalId, long studentId,
                                   String status)
            throws SQLException {

        if (goalId <= 0) {
            throw new IllegalArgumentException(
                    "Goal ID must be greater than zero.");
        }

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero.");
        }

        String normalizedStatus = validateStatus(status);

        return goalDAO.updateGoalStatus(
                goalId, studentId, normalizedStatus);
    }
}
