
package com.skilltwin.service;

import com.skilltwin.dao.LearningScenarioDAO;
import com.skilltwin.model.LearningScenario;

import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;

public class ScenarioComparisonService {

    private final LearningScenarioDAO scenarioDAO;

    public ScenarioComparisonService() {
        this.scenarioDAO = new LearningScenarioDAO();
    }

    public List<LearningScenario> compareScenarios(long studentId)
            throws SQLException {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero.");
        }

        List<LearningScenario> scenarios =
                scenarioDAO.getScenariosByStudentId(studentId);

        scenarios.sort(
                Comparator.comparing(
                        LearningScenario::getEstimatedScore,
                        Comparator.nullsLast(Comparator.reverseOrder()))
                .thenComparingInt(LearningScenario::getDurationWeeks));

        return scenarios;
    }
}
