
package com.skilltwin.service;

import com.skilltwin.dao.LearningGoalDAO;
import com.skilltwin.dao.ProgressDAO;
import com.skilltwin.dao.SkillDAO;
import com.skilltwin.model.LearningGoal;
import com.skilltwin.model.ProgressRecord;
import com.skilltwin.model.Skill;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class LearningSummaryService {

    private final LearningGoalDAO goalDAO = new LearningGoalDAO();
    private final ProgressDAO progressDAO = new ProgressDAO();
    private final SkillDAO skillDAO = new SkillDAO();

    public void displaySummary(long studentId) throws SQLException {

        List<LearningGoal> goals =
                goalDAO.getGoalsByStudentId(studentId);

        List<ProgressRecord> records =
                progressDAO.getProgressByStudentId(studentId);

        List<Skill> skills =
                skillDAO.getSkillsByStudentId(studentId);

        int totalGoals = goals.size();
        int completedGoals = 0;

        for (LearningGoal goal : goals) {
            if ("COMPLETED".equalsIgnoreCase(goal.getStatus())) {
                completedGoals++;
            }
        }

        int remainingGoals = totalGoals - completedGoals;

        BigDecimal totalHours = BigDecimal.ZERO;

        for (ProgressRecord record : records) {
            if (record.getHoursCompleted() != null) {
                totalHours = totalHours.add(
                        record.getHoursCompleted());
            }
        }

        System.out.println("\n========== LEARNING PROGRESS SUMMARY ==========");
        System.out.println("Student ID: " + studentId);

        System.out.println("\n--- Learning Goals ---");
        System.out.println("Total Goals: " + totalGoals);
        System.out.println("Completed Goals: " + completedGoals);
        System.out.println("Remaining Goals: " + remainingGoals);

        System.out.println("\n--- Skill Progress ---");

        if (skills.isEmpty()) {
            System.out.println("No skills registered.");
        } else {
            for (Skill skill : skills) {
                int current = skill.getCurrentLevel();
                int target = skill.getTargetLevel();
                int remaining = Math.max(0, target - current);

                System.out.println("Skill: " + skill.getSkillName());
                System.out.println("Current Level: " + current);
                System.out.println("Target Level: " + target);
                System.out.println("Points Remaining: " + remaining);
                System.out.println("-----------------------------------------------");
            }
        }

        System.out.println("\n--- Study Activity ---");
        System.out.println("Progress Records: " + records.size());
        System.out.println("Total Recorded Study Hours: " + totalHours);

        System.out.println("================================================");
    }
}
