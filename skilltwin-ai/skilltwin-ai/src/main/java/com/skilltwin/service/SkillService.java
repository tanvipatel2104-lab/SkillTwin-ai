package com.skilltwin.service;

import com.skilltwin.dao.SkillDAO;
import com.skilltwin.model.Skill;

import java.sql.SQLException;
import java.util.List;

public class SkillService {

    private final SkillDAO skillDAO;

    public SkillService() {
        this.skillDAO = new SkillDAO();
    }

    public long addSkill(long studentId, String skillName,
                         int currentLevel, int targetLevel)
            throws SQLException {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero.");
        }

        if (skillName == null || skillName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "Skill name cannot be empty.");
        }

        skillName = skillName.trim();

        if (skillName.length() > 100) {
            throw new IllegalArgumentException(
                    "Skill name cannot exceed 100 characters.");
        }

        if (currentLevel < 0 || currentLevel > 100) {
            throw new IllegalArgumentException(
                    "Current level must be between 0 and 100.");
        }

        if (targetLevel < 0 || targetLevel > 100) {
            throw new IllegalArgumentException(
                    "Target level must be between 0 and 100.");
        }

        Skill skill = new Skill(
                studentId,
                skillName,
                currentLevel,
                targetLevel
        );

        return skillDAO.addSkill(skill);
    }

    public List<Skill> getSkillsByStudentId(long studentId)
            throws SQLException {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero.");
        }

        return skillDAO.getSkillsByStudentId(studentId);
    }
}