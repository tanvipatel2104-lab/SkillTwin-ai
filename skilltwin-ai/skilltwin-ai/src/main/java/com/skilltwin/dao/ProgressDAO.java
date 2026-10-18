package com.skilltwin.dao;

import com.skilltwin.config.DatabaseConnection;
import com.skilltwin.model.ProgressRecord;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ProgressDAO {
    public long saveProgress(ProgressRecord record)
            throws SQLException {
        String sql = "INSERT INTO progress_records " + "(student_id, skill_id, hours_completed, " + "skill_level_after, notes) " + "VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement( sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setLong(1, record.getStudentId());
            if (record.getSkillId() == null) {
                statement.setNull(2, java.sql.Types.BIGINT);
            } 
            else {
                statement.setLong(2, record.getSkillId());
            }
            statement.setBigDecimal(3, record.getHoursCompleted());
            if (record.getSkillLevelAfter() == null) {
                statement.setNull(4, java.sql.Types.INTEGER);
            } 
            else {
                statement.setInt(4, record.getSkillLevelAfter());
            }
            statement.setString(5, record.getNotes());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next())
                {
                    return keys.getLong(1);
                }
            }
        }
        throw new SQLException("Progress saved, but its ID was not returned.");
    }
    public List<ProgressRecord> getProgressByStudentId(long studentId) throws SQLException {
        List<ProgressRecord> records = new ArrayList<>();
        String sql = "SELECT progress_id, student_id, skill_id, "
                + "hours_completed, skill_level_after, notes, recorded_at "
                + "FROM progress_records "
                + "WHERE student_id = ? "
                + "ORDER BY recorded_at DESC, progress_id DESC";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, studentId);
            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    ProgressRecord record = new ProgressRecord();
                    record.setProgressId(rs.getLong("progress_id"));
                    record.setStudentId(rs.getLong("student_id"));
                    long skillId = rs.getLong("skill_id");
                    record.setSkillId(rs.wasNull() ? null : skillId);
                    record.setHoursCompleted(rs.getBigDecimal("hours_completed"));
                    int level = rs.getInt("skill_level_after");
                    record.setSkillLevelAfter(rs.wasNull() ? null : level);
                    record.setNotes(rs.getString("notes"));
                    Timestamp recordedAt = rs.getTimestamp("recorded_at");
                    record.setRecordedAt(recordedAt);
                    records.add(record);
                }
            }
        }
        return records;
    }
public long saveProgressAndUpdateSkill(ProgressRecord record)
        throws SQLException {
    String insertSql = "INSERT INTO progress_records "
            + "(student_id, skill_id, hours_completed, "
            + "skill_level_after, notes) VALUES (?, ?, ?, ?, ?)";
    String updateSql = "UPDATE skills SET current_level = ? "
            + "WHERE student_id = ? AND skill_id = ?";
    try (Connection connection = DatabaseConnection.getConnection()) {
        connection.setAutoCommit(false);
        try {
            long progressId;
            // 1. Save the historical progress record.
            try (PreparedStatement statement = connection.prepareStatement( insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                statement.setLong(1, record.getStudentId());
                if (record.getSkillId() == null) {
                    statement.setNull(2, java.sql.Types.BIGINT);
                } 
                else {
                    statement.setLong(2, record.getSkillId());
                }
                statement.setBigDecimal(3, record.getHoursCompleted());
                if (record.getSkillLevelAfter() == null) {
                    statement.setNull(4, java.sql.Types.INTEGER);
                } 
                else {
                    statement.setInt(4, record.getSkillLevelAfter());
                }
                statement.setString(5, record.getNotes());
                statement.executeUpdate();
                try (ResultSet keys = statement.getGeneratedKeys()) {
                    if (!keys.next()) {
                        throw new SQLException("Progress record ID was not returned.");
                    }
                    progressId = keys.getLong(1);
                }
            }
            // 2. Update the current skill level when provided.
            if (record.getSkillId() != null && record.getSkillLevelAfter() != null) {
                try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
                    statement.setInt(1, record.getSkillLevelAfter());
                    statement.setLong(2, record.getStudentId());
                    statement.setLong(3, record.getSkillId());
                    if (statement.executeUpdate() != 1) {
                        throw new SQLException("Current skill level could not be updated.");
                    }
                }
            }
            // 3. Commit both operations together.
            connection.commit();
            return progressId;
        } 
        catch (SQLException | RuntimeException e) {
            try {
                connection.rollback();
            } 
            catch (SQLException rollbackError) {
                e.addSuppressed(rollbackError);
            }
            throw e;
        }
    }
}
}
