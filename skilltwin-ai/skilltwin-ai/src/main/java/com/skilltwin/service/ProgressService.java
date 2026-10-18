
package com.skilltwin.service;

import com.skilltwin.dao.ProgressDAO;
import com.skilltwin.dao.SimulationDAO;
import com.skilltwin.dao.SkillDAO;
import com.skilltwin.model.ProgressRecord;
import com.skilltwin.model.Skill;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProgressService {

    private final ProgressDAO progressDAO;
    private final SimulationDAO simulationDAO;
    private final SkillDAO skillDAO;

    public ProgressService() {
        this.progressDAO = new ProgressDAO();
        this.simulationDAO = new SimulationDAO();
        this.skillDAO = new SkillDAO();
    }

    public long recordProgress(ProgressRecord record)
            throws SQLException {

        if (record == null) {
            throw new IllegalArgumentException(
                    "Progress record cannot be null.");
        }

        if (record.getStudentId() <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero.");
        }

        if (simulationDAO.getStudentById(record.getStudentId()) == null) {
            throw new IllegalArgumentException(
                    "Student ID does not exist.");
        }

        BigDecimal hours = record.getHoursCompleted();

        if (hours == null
                || hours.compareTo(BigDecimal.ZERO) < 0
                || hours.compareTo(new BigDecimal("24")) > 0) {
            throw new IllegalArgumentException(
                    "Completed hours must be between 0 and 24.");
        }

        if (record.getSkillId() != null) {
            if (record.getSkillId() <= 0) {
                throw new IllegalArgumentException(
                        "Skill ID must be greater than zero.");
            }

            List<Skill> skills = skillDAO.getSkillsByStudentId(
                    record.getStudentId());

            boolean skillBelongsToStudent = false;

            for (Skill skill : skills) {
                if (skill.getSkillId() == record.getSkillId()) {
                    skillBelongsToStudent = true;
                    break;
                }
            }

            if (!skillBelongsToStudent) {
                throw new IllegalArgumentException(
                        "Skill ID does not belong to this student.");
            }
        }

        Integer level = record.getSkillLevelAfter();

        if (level != null && (level < 0 || level > 100)) {
            throw new IllegalArgumentException(
                    "Skill level must be between 0 and 100.");
        }

        if (record.getNotes() != null
                && record.getNotes().length() > 5000) {
            throw new IllegalArgumentException(
                    "Notes cannot exceed 5000 characters.");
        }

        return progressDAO.saveProgressAndUpdateSkill(record);
    }

    public List<ProgressRecord> getProgressHistory(long studentId)
            throws SQLException {

        if (studentId <= 0) {
            throw new IllegalArgumentException(
                    "Student ID must be greater than zero.");
        }

        if (simulationDAO.getStudentById(studentId) == null) {
            throw new IllegalArgumentException(
                    "Student ID does not exist.");
        }

        return progressDAO.getProgressByStudentId(studentId);
    }
}
