package com.skilltwin.model;

import java.time.LocalDate;

public class LearningGoal {
    private long goalId;
    private long studentId;
    private String goalTitle;
    private String description;
    private LocalDate targetDate;
    private String status;
    public LearningGoal() {
    }
    public LearningGoal(long studentId, String goalTitle, String description, LocalDate targetDate, String status) {
        this.studentId = studentId;
        this.goalTitle = goalTitle;
        this.description = description;
        this.targetDate = targetDate;
        this.status = status;
    }
    public long getGoalId() {
        return goalId;
    }
    public void setGoalId(long goalId) {
        this.goalId = goalId;
    }
    public long getStudentId() {
        return studentId;
    }
    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }
    public String getGoalTitle() {
        return goalTitle;
    }
    public void setGoalTitle(String goalTitle) {
        this.goalTitle = goalTitle;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDate getTargetDate() {
        return targetDate;
    }
    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    @Override
    public String toString() {
        return "Goal ID: " + goalId
                + " | Student ID: " + studentId
                + " | Title: " + goalTitle
                + " | Target Date: "
                + (targetDate == null ? "Not set" : targetDate)
                + " | Status: " + status
                + " | Description: "
                + (description == null || description.isBlank()
                   ? "Not provided" : description);
    }
}