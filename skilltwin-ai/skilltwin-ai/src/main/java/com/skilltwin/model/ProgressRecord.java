package com.skilltwin.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class ProgressRecord {
    private long progressId;
    private long studentId;
    private Long skillId;
    private BigDecimal hoursCompleted;
    private Integer skillLevelAfter;
    private String notes;
    private Timestamp recordedAt;
    public ProgressRecord() {
    }
    public long getProgressId() {
        return progressId;
    }
    public void setProgressId(long progressId) {
        this.progressId = progressId;
    }
    public long getStudentId() {
        return studentId;
    }
    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }
    public Long getSkillId() {
        return skillId;
    }
    public void setSkillId(Long skillId) {
        this.skillId = skillId;
    }
    public BigDecimal getHoursCompleted() {
        return hoursCompleted;
    }
    public void setHoursCompleted(BigDecimal hoursCompleted) {
        this.hoursCompleted = hoursCompleted;
    }
    public Integer getSkillLevelAfter() {
        return skillLevelAfter;
    }
    public void setSkillLevelAfter(Integer skillLevelAfter) {
        this.skillLevelAfter = skillLevelAfter;
    }
    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
    public Timestamp getRecordedAt() {
        return recordedAt;
    }
    public void setRecordedAt(Timestamp recordedAt) {
        this.recordedAt = recordedAt;
    }
}
