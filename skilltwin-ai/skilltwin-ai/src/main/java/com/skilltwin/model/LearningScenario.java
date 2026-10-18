
package com.skilltwin.model;

import java.math.BigDecimal;

public class LearningScenario {

    private long scenarioId;
    private long studentId;
    private String scenarioName;
    private int durationWeeks;
    private BigDecimal estimatedScore;

    public LearningScenario() {
    }

    public LearningScenario(long scenarioId, long studentId,
                            String scenarioName, int durationWeeks,
                            BigDecimal estimatedScore) {
        this.scenarioId = scenarioId;
        this.studentId = studentId;
        this.scenarioName = scenarioName;
        this.durationWeeks = durationWeeks;
        this.estimatedScore = estimatedScore;
    }

    public long getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(long scenarioId) {
        this.scenarioId = scenarioId;
    }

    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    public BigDecimal getEstimatedScore() {
        return estimatedScore;
    }

    public void setEstimatedScore(BigDecimal estimatedScore) {
        this.estimatedScore = estimatedScore;
    }
}
