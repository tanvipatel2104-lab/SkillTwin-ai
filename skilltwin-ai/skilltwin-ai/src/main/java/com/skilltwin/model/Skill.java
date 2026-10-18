package com.skilltwin.model;

public class Skill {

    private long skillId;
    private long studentId;
    private String skillName;
    private int currentLevel;
    private int targetLevel;

    public Skill() {
    }

    public Skill(long studentId, String skillName,
                 int currentLevel, int targetLevel) {
        this.studentId = studentId;
        this.skillName = skillName;
        this.currentLevel = currentLevel;
        this.targetLevel = targetLevel;
    }

    public long getSkillId() {
        return skillId;
    }

    public void setSkillId(long skillId) {
        this.skillId = skillId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getSkillName() {
        return skillName;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public int getTargetLevel() {
        return targetLevel;
    }

    public void setTargetLevel(int targetLevel) {
        this.targetLevel = targetLevel;
    }

    @Override
    public String toString() {
        return "Skill ID: " + skillId
                + " | Student ID: " + studentId
                + " | Skill: " + skillName
                + " | Current Level: " + currentLevel
                + " | Target Level: " + targetLevel;
    }
}