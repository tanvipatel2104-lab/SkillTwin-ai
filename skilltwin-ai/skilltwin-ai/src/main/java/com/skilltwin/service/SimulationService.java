package com.skilltwin.service;

import com.skilltwin.dao.SimulationDAO;
import com.skilltwin.model.Skill;
import com.skilltwin.model.Student;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

public class SimulationService {

    private final SimulationDAO simulationDAO;

    // Demonstration assumption; calibrate using actual learning data later.
    private static final BigDecimal POINTS_PER_HOUR =
            new BigDecimal("2.0");

    private static final BigDecimal DAYS_PER_WEEK =
            new BigDecimal("7");

    public SimulationService() {
        this.simulationDAO = new SimulationDAO();
    }

    public void simulateLearning(long studentId,
                                 String scenarioName,
                                 int durationWeeks)
            throws SQLException {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero.");
        }

        if (scenarioName == null || scenarioName.isBlank()) {
            throw new IllegalArgumentException(
                    "Scenario name cannot be empty.");
        }

        scenarioName = scenarioName.trim();

        if (scenarioName.length() > 150) {
            throw new IllegalArgumentException(
                    "Scenario name cannot exceed 150 characters.");
        }

        if (durationWeeks < 1 || durationWeeks > 104) {
            throw new IllegalArgumentException(
                    "Duration must be between 1 and 104 weeks.");
        }

        Student student = simulationDAO.getStudentById(studentId);

        if (student == null) {
            throw new IllegalArgumentException(
                    "Student ID " + studentId + " does not exist.");
        }

        List<Skill> skills =
                simulationDAO.getSkillsByStudentId(studentId);

        if (skills.isEmpty()) {
            throw new IllegalArgumentException(
                    "This student has no skills. Add a skill first.");
        }

        BigDecimal hoursPerDay = student.getAvailableHoursPerDay();

        if (hoursPerDay == null
                || hoursPerDay.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Student's available study hours must be greater than zero.");
        }

        BigDecimal totalStudyHours = hoursPerDay
                .multiply(DAYS_PER_WEEK)
                .multiply(BigDecimal.valueOf(durationWeeks));

        BigDecimal totalPoints = totalStudyHours
                .multiply(POINTS_PER_HOUR);

        BigDecimal scoreSum = BigDecimal.ZERO;
        int validSkillCount = 0;

        System.out.println("\n================================");
        System.out.println("     DIGITAL TWIN SIMULATION");
        System.out.println("================================");
        System.out.println("Student: " + student.getFullName());
        System.out.println("Scenario: " + scenarioName);
        System.out.println("Duration: " + durationWeeks + " weeks");
        System.out.println("Study hours per day: " + hoursPerDay);
        System.out.println("Estimated total study hours: "
                + totalStudyHours.setScale(2, RoundingMode.HALF_UP));
        System.out.println("\n--- Projected Skill Outcomes ---");

        for (Skill skill : skills) {

            int current = skill.getCurrentLevel();
            int target = skill.getTargetLevel();

            BigDecimal share = BigDecimal.ONE.divide(
                    BigDecimal.valueOf(skills.size()),
                    4,
                    RoundingMode.HALF_UP);

            BigDecimal allocatedPoints = totalPoints.multiply(share);

            int projected = current;

            if (target > current) {
                projected = Math.min(
                        target,
                        current + allocatedPoints
                                .setScale(0, RoundingMode.FLOOR)
                                .intValue());
            }

            int remainingGap = Math.max(0, target - projected);

            double progress;

            if (target <= 0 || projected >= target) {
                progress = 100.0;
            } else {
                progress = projected * 100.0 / target;
            }

            System.out.println("\nSkill: " + skill.getSkillName());
            System.out.println("Current level: " + current);
            System.out.println("Target level: " + target);
            System.out.println("Projected level: " + projected);
            System.out.println("Remaining gap: " + remainingGap);
            System.out.printf("Projected target progress: %.2f%%%n",
                    progress);

            scoreSum = scoreSum.add(
                    BigDecimal.valueOf(projected));
            validSkillCount++;
        }

        BigDecimal estimatedScore = scoreSum
                .divide(BigDecimal.valueOf(validSkillCount),
                        2, RoundingMode.HALF_UP);

        long scenarioId = simulationDAO.saveScenario(
                studentId,
                scenarioName,
                durationWeeks,
                estimatedScore);

        System.out.println("\n--------------------------------");
        System.out.println("Estimated average skill score: "
                + estimatedScore + "/100");
        System.out.println("Scenario saved successfully.");
        System.out.println("Scenario ID: " + scenarioId);
        System.out.println("Note: This is a rule-based estimate,");
        System.out.println("not a guarantee of actual learning.");
        System.out.println("================================");
    }
}